package com.ms.login.infrastructure.config.domainService;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.domain.domainService.implementation.LoginDomainServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoginDomainServiceConfig {

    @Bean
    public LoginDomainServiceImpl loginDomainService(LoginGateway loginGateway){
        return new  LoginDomainServiceImpl(loginGateway);
    }
}
