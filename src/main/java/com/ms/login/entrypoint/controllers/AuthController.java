package com.ms.login.entrypoint.controllers;

import com.ms.login.entrypoint.controllers.dto.LoginRequestDTO;
import com.ms.login.entrypoint.controllers.dto.LoginResponse;
import com.ms.login.infrastructure.config.security.*;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;


@RestController
@RequestMapping("/v1")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final SecurityAuditLogger  securityAuditLogger;
    private final RateLimitingFilter  rateLimitingFilter;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          SecurityAuditLogger securityAuditLogger,
                          RateLimitingFilter rateLimitingFilter,
                          JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.securityAuditLogger = securityAuditLogger;
        this.rateLimitingFilter = rateLimitingFilter;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/public")
    public String teste(){
        return "AAAAAAAAAAAAAAAAAAAAAa funcionou";
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequestDTO loginRequest, HttpServletRequest request) {
        String clientIp = getClientIpAddress(request);

        //Validação de segurança antes da realização da autenticação
        if(InputValidationFilter.containsMaliciousContent(loginRequest.username()) ||
                InputValidationFilter.containsMaliciousContent(loginRequest.password())){

            securityAuditLogger.logSuspiciousActivity(loginRequest.username(), clientIp,
                    "MALICIOUS_INPUT", "Attempted login with malicious input");
            throw new BadCredentialsException("Invalid credentials");
        }

        try{
            //Realiza a autenticação
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.username(), loginRequest.password()));

            //Objeto de usuario autenticado
            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();
            String token = jwtTokenProvider.generateToken(myUserDetails);

            Claims claims = jwtTokenProvider.extractClaims(token);
            Date expiresAt = jwtTokenProvider.extractExpirationDate(claims);
            String userId = jwtTokenProvider.extractUserId(claims);

            //Log de Sucesso
            securityAuditLogger.logLoginAttempt(loginRequest.username(), clientIp, true, "SUCCESS");

            //Limpa as tentativas que não deram certo
            rateLimitingFilter.clearAttempts(clientIp);
            return new LoginResponse(token, authentication.getName(), expiresAt.toString(), userId);

        }catch (BadCredentialsException e) {
            // Log de falha
            securityAuditLogger.logLoginAttempt(loginRequest.username(), clientIp, false, "INVALID_CREDENTIALS");
            rateLimitingFilter.recordFailedAttempt(clientIp);
            throw e;
        } catch (Exception e) {
            securityAuditLogger.logLoginAttempt(loginRequest.username(), clientIp, false, "SYSTEM_ERROR");
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
