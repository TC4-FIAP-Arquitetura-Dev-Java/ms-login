package com.ms.login.domain.domainService.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.domain.domainService.LoginDomainService;
import com.ms.login.domain.exceptions.CredentialLoginAlreadyExistsException;

public class LoginDomainServiceImpl implements LoginDomainService {

    private final LoginGateway loginGateway;

    public LoginDomainServiceImpl(LoginGateway loginGateway) {
        this.loginGateway = loginGateway;
    }

    @Override
    public void checkExistsUsername(String username) {
        if(loginGateway.getUsername(username).isPresent()) {
            throw new CredentialLoginAlreadyExistsException("Username already exists");
        }
    }
}
