package com.ms.login.application.usecase.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.gateway.SecretKeyGenerator;
import com.ms.login.application.usecase.CreateLoginUseCase;
import com.ms.login.domain.domainService.LoginDomainService;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.domain.rules.RequiredFieldsRule;

public class CreateLoginUseCaseImpl implements CreateLoginUseCase {

    private final LoginGateway loginGateway;
    private final LoginDomainService loginDomainService;
    private final SecretKeyGenerator secretKeyGenerator;

    public CreateLoginUseCaseImpl(LoginGateway loginGateway,
                                  LoginDomainService loginDomainService,
                                  SecretKeyGenerator secretKeyGenerator) {
        this.loginGateway = loginGateway;
        this.loginDomainService = loginDomainService;
        this.secretKeyGenerator = secretKeyGenerator;
    }

    @Override
    public void register(LoginDomain loginDomain) {
        RequiredFieldsRule.checkRequiredFields(loginDomain);
        loginDomainService.checkExistsUsername(loginDomain.getUsername());
        loginDomain.setUsername(loginDomain.getUsername().toLowerCase());
        loginDomain.setPassword(secretKeyGenerator.encode(loginDomain.getPassword()));
        loginGateway.register(loginDomain);
    }
}
