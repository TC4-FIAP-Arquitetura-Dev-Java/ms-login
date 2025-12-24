package com.ms.login.infrastructure.config.usecase;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.application.usecase.implementation.RegisterUserUseCaseImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisterConfig {

    @Bean
    public RegisterUserUseCaseImpl RegisterConfig(UserGateway userGateway) {
        return new RegisterUserUseCaseImpl(userGateway);
    }
}
