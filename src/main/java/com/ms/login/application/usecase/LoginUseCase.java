package com.ms.login.application.usecase;

import com.ms.login.domain.model.AuthTokenDomain;

public interface LoginUseCase {

    AuthTokenDomain login(String username, String password);

}
