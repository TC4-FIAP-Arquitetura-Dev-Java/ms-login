package com.ms.login.application.gateway;

import com.ms.login.domain.model.LoginDomain;

import java.util.Optional;

public interface LoginGateway {

    void register(LoginDomain loginDomain);

    Optional<LoginDomain> getUsername(String username);
}
