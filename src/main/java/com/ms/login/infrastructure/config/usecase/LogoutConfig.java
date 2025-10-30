package com.ms.login.infrastructure.config.usecase;

import com.ms.login.application.usecase.SessionTokenUseCase;
import com.ms.login.application.usecase.implementation.LogoutUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogoutConfig {

    @Bean
    public LogoutUseCaseImpl logoutUseCase(SessionTokenUseCase sessionTokenUseCase) {
        return new LogoutUseCaseImpl(sessionTokenUseCase);
    }
}
