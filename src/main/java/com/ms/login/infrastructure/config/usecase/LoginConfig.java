package com.ms.login.infrastructure.config.usecase;

import com.ms.login.application.usecase.implementation.LoginUseCaseImpl;
import com.ms.login.infrastructure.security.ClientIpResolver;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import com.ms.login.infrastructure.security.RateLimitingFilter;
import com.ms.login.infrastructure.security.SecurityAuditLogger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;

@Configuration
public class LoginConfig {

    @Bean
    public LoginUseCaseImpl loginUseCase(AuthenticationManager authenticationManager,
                            JwtTokenProvider jwtTokenProvider,
                            SecurityAuditLogger securityAuditLogger,
                            RateLimitingFilter rateLimitingFilter,
                            ClientIpResolver clientIpResolver) {
        return new LoginUseCaseImpl(authenticationManager, jwtTokenProvider, securityAuditLogger, rateLimitingFilter, clientIpResolver);
    }
}
