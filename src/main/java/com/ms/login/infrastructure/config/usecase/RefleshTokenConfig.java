package com.ms.login.infrastructure.config.usecase;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.usecase.implementation.RefleshTokenUseCaseImpl;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefleshTokenConfig {

    @Bean
    public RefleshTokenUseCaseImpl refreshTokenUseCase(JwtTokenProvider jwtTokenProvider,
                                                       LoginGateway loginGateway) {
        return new RefleshTokenUseCaseImpl(jwtTokenProvider, loginGateway);
    }
}
