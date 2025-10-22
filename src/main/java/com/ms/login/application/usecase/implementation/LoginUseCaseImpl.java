package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.LoginUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.CredentialDomain;
import com.ms.login.infrastructure.config.security.*;
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

    public LoginUseCaseImpl(AuthenticationManager authenticationManager,
                            JwtTokenProvider jwtTokenProvider,
                            SecurityAuditLogger securityAuditLogger,
                            RateLimitingFilter rateLimitingFilter) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.securityAuditLogger = securityAuditLogger;
        this.rateLimitingFilter = rateLimitingFilter;
    }

    @Override
    public AuthTokenDomain login(CredentialDomain credentialDomain) {

//        String clientIp = getClientIpAddress(null);
        //TODO: Corrigir essa parte
        String clientIp = "";

        if (InputValidationFilter.containsMaliciousContent(credentialDomain.getUsername()) ||
                InputValidationFilter.containsMaliciousContent(credentialDomain.getPassword())) {

            securityAuditLogger.logSuspiciousActivity(
                    credentialDomain.getUsername(), clientIp, "MALICIOUS_INPUT",
                    "Attempted login with malicious input");
            throw new BadCredentialsException("Invalid credentials");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            credentialDomain.getUsername(), credentialDomain.getPassword()));

            MyUserDetails myUserDetails = (MyUserDetails) authentication.getPrincipal();

            String token = jwtTokenProvider.generateAccessToken(myUserDetails.getUsername());
            Claims claims = jwtTokenProvider.extractClaims(token);
            Date expiresAt = jwtTokenProvider.extractExpirationDate(claims);
            String userId = jwtTokenProvider.extractUserId(claims);

            securityAuditLogger.logLoginAttempt(credentialDomain.getUsername(), clientIp, true, "SUCCESS");
            rateLimitingFilter.clearAttempts(clientIp);

            AuthTokenDomain response = new AuthTokenDomain(token, authentication.getName(), expiresAt.toString(), userId);
            return response;

        } catch (BadCredentialsException e) {
            securityAuditLogger.logLoginAttempt(credentialDomain.getUsername(), clientIp, false, "INVALID_CREDENTIALS");
            rateLimitingFilter.recordFailedAttempt(clientIp);
            throw e;
        } catch (Exception e) {
            securityAuditLogger.logLoginAttempt(credentialDomain.getUsername(), clientIp, false, "SYSTEM_ERROR");
            throw new BadCredentialsException("Authentication failed");
        }
    }
}
