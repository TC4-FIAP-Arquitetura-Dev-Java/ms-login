package com.ms.login.application.gateway;

import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.CredentialDomain;
import com.ms.login.domain.model.LoginDomain;

import java.util.Optional;

public interface LoginGateway {

    Optional<LoginDomain> register(LoginDomain loginDomain);

    AuthTokenDomain authenticate(CredentialDomain credentials);

    AuthTokenDomain refreshToken(String refreshToken);

    void invalidateTokens(String userId);
}
