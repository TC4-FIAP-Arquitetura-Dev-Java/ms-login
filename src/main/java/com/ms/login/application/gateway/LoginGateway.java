package com.ms.login.application.gateway;

import com.ms.login.domain.model.LoginDomain;

import java.util.Optional;

public interface LoginGateway {

    void register(LoginDomain loginDomain);

//    Optional<LoginDomain> authenticate(CredentialDomain credentials);
//
//    AuthTokenDomain refreshToken(String refreshToken);
//    void invalidateTokens(String userId);

    //Manter temporariamente ate definir a   estrutura
    Optional<LoginDomain> getUsername(String username);
}
