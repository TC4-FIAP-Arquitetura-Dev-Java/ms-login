package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.LoginUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.infrastructure.security.*;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

public class LoginUseCaseImpl implements LoginUseCase {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final SecurityAuditLogger securityAuditLogger;
    private final RateLimitingFilter rateLimitingFilter;
    private final ClientIpResolver clientIpResolver;

    public LoginUseCaseImpl(AuthenticationManager authenticationManager,
                            JwtTokenProvider jwtTokenProvider,
                            SecurityAuditLogger securityAuditLogger,
                            RateLimitingFilter rateLimitingFilter,
                            ClientIpResolver clientIpResolver) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityAuditLogger = securityAuditLogger;
        this.rateLimitingFilter = rateLimitingFilter;
        this.clientIpResolver = clientIpResolver;
    }

    @Override
    public AuthTokenDomain login(String username, String password) {
        String clientIp = clientIpResolver.resolve();

        // Validação de inputs maliciosos
        if (InputValidationFilter.containsMaliciousContent(username) ||
                InputValidationFilter.containsMaliciousContent(password)) {

            securityAuditLogger.logSuspiciousActivity(username, clientIp, "MALICIOUS_INPUT",
                    "Attempted login with malicious input");
            throw new BadCredentialsException("Invalid credentials");
        }

        try {
            // Autenticação
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();

            // Geração do token JWT com claims completos
            String token = jwtTokenProvider.generateAccessToken(userDetails.getUsername(), userDetails.getUserId(), userDetails.getRoleEnum());
            String refreshToken = jwtTokenProvider.generateRefreshToken(userDetails.getUsername());

            Claims claims = jwtTokenProvider.extractClaimsAcessToken(token);
            Date expiresAt = jwtTokenProvider.extractExpirationDate(claims);
            String userId = jwtTokenProvider.extractUserId(claims);
            String role = jwtTokenProvider.extractRole(claims).name();

            // Auditoria e rate limit
            securityAuditLogger.logLoginAttempt(username, clientIp, true, "SUCCESS");
            rateLimitingFilter.clearAttempts(clientIp);

            return new AuthTokenDomain(token, refreshToken, userDetails.getUsername(), role, userId, expiresAt.toString());

        } catch (BadCredentialsException e) {
            securityAuditLogger.logLoginAttempt(username, clientIp, false, "INVALID_CREDENTIALS");
            rateLimitingFilter.recordFailedAttempt(clientIp);
            throw e;
        } catch (Exception e) {
            securityAuditLogger.logLoginAttempt(username, clientIp, false, "SYSTEM_ERROR");
            throw new BadCredentialsException("Authentication failed", e);
        }
    }
}
