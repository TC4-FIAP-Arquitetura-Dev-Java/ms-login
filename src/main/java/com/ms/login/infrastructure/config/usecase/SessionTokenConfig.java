package com.ms.login.infrastructure.config.usecase;

import com.ms.login.application.usecase.implementation.SessionTokenUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionTokenConfig {

    @Bean
    public SessionTokenUseCaseImpl sessionToken(){
        return new SessionTokenUseCaseImpl();
    }
}
