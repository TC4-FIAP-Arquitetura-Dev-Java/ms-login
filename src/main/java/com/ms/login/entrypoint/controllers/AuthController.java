package com.ms.login.entrypoint.controllers;

import com.ms.login.application.gateway.UserClient;
import com.ms.login.infrastructure.config.security.*;
import com.ms.loginDomain.LoginApi;
import com.ms.loginDomain.gen.model.LoginRequestDto;
import com.ms.loginDomain.gen.model.LoginResponseDto;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/v1")
public class AuthController implements LoginApi {

    private final AuthenticationManager authenticationManager;
    private final SecurityAuditLogger  securityAuditLogger;
    private final RateLimitingFilter  rateLimitingFilter;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserClient userClient;

    public AuthController(AuthenticationManager authenticationManager,
                          SecurityAuditLogger securityAuditLogger,
                          RateLimitingFilter rateLimitingFilter,
                          JwtTokenProvider jwtTokenProvider,
                          UserClient userClient) {
        this.authenticationManager = authenticationManager;
        this.securityAuditLogger = securityAuditLogger;
        this.rateLimitingFilter = rateLimitingFilter;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userClient = userClient;
    }

    @Override
    public ResponseEntity<LoginResponseDto> _login(LoginRequestDto loginRequest) {
        String clientIp = getClientIpAddress(null);

        //Validação de segurança antes da realização da autenticação
        if(InputValidationFilter.containsMaliciousContent(loginRequest.getUsername()) ||
                InputValidationFilter.containsMaliciousContent(loginRequest.getPassword())){

            securityAuditLogger.logSuspiciousActivity(loginRequest.getUsername(), clientIp,
                    "MALICIOUS_INPUT", "Attempted login with malicious input");
            throw new BadCredentialsException("Invalid credentials");
        }

        try{
            //Realiza a autenticação - De foma automatica ele acessa o MyUserDetailsService -> loadUserByUsername -> verificando a existencia para assim continuar com o autenticação
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            //Objeto de usuario autenticado
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(myUserDetails);

            Claims claims = jwtTokenProvider.extractClaims(token);
            Date expiresAt = jwtTokenProvider.extractExpirationDate(claims);
            String userId = jwtTokenProvider.extractUserId(claims);

            //Log de Sucesso
            securityAuditLogger.logLoginAttempt(loginRequest.getUsername(), clientIp, true, "SUCCESS");

            //Limpa as tentativas que não deram certo
            rateLimitingFilter.clearAttempts(clientIp);

            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setToken(token);
            responseDto.setUsername(authentication.getName());
            responseDto.setExpiresAt(expiresAt.toString());
            responseDto.setUserId(userId);
            return ResponseEntity.ok(responseDto);

        }catch (BadCredentialsException e) {
            // Log de falha
            securityAuditLogger.logLoginAttempt(loginRequest.getUsername(), clientIp, false, "INVALID_CREDENTIALS");
            rateLimitingFilter.recordFailedAttempt(clientIp);
            throw e;
        } catch (Exception e) {
            securityAuditLogger.logLoginAttempt(loginRequest.getUsername(), clientIp, false, "SYSTEM_ERROR");
            throw new BadCredentialsException("Authentication failed");
        }
    }

    private String getClientIpAddress(HttpServletRequest request) {
        //Obtem o endereço ip de quem esta realizando o login
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if(xForwardedFor != null && !xForwardedFor.isEmpty()){
            return xForwardedFor.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}
