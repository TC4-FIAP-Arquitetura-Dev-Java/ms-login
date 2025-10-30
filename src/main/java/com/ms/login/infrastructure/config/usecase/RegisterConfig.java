package com.ms.login.infrastructure.config.usecase;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.gateway.SecretKeyGenerator;
import com.ms.login.application.usecase.implementation.CreateLoginUseCaseImpl;
import com.ms.login.domain.domainService.LoginDomainService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RegisterConfig {

    @Bean
    public CreateLoginUseCaseImpl RegisterConfig(LoginGateway loginGateway,
                                                 LoginDomainService loginDomainService,
                                                 SecretKeyGenerator secretKeyGenerator) {
        return new CreateLoginUseCaseImpl(loginGateway, loginDomainService, secretKeyGenerator);
    }
}
