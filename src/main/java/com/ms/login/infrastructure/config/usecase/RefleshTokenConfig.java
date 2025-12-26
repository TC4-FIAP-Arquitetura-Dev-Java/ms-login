package com.ms.login.infrastructure.config.usecase;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.application.usecase.SessionTokenUseCase;
import com.ms.login.application.usecase.implementation.RefleshUseCaseImpl;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RefleshTokenConfig {

    @Bean
    public RefleshUseCaseImpl refreshTokenUseCase(JwtTokenProvider jwtTokenProvider,
                                                  UserGateway userGateway,
                                                  SessionTokenUseCase sessionTokenUseCase) {
        return new RefleshUseCaseImpl(jwtTokenProvider, userGateway, sessionTokenUseCase);
    }
}
