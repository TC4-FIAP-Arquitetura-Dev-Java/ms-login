package com.ms.login.infrastructure.config.database.implementations;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.CredentialDomain;
import com.ms.login.domain.model.LoginDomain;

import java.util.Optional;

public class LoginGatewayImpl implements LoginGateway {

    @Override
    public Optional<LoginDomain> register(LoginDomain loginDomain) {
        return Optional.empty();
    }

    @Override
    public AuthTokenDomain authenticate(CredentialDomain credentials) {
        return null;
    }

    @Override
    public AuthTokenDomain refreshToken(String refreshToken) {
        return null;
    }

    @Override
    public void invalidateTokens(String userId) {

    }
}
