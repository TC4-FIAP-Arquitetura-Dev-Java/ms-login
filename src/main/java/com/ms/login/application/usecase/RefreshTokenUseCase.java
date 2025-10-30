package com.ms.login.application.usecase;

import com.ms.login.domain.model.AuthTokenDomain;

public interface RefreshTokenUseCase {

    AuthTokenDomain refreshToken(String refreshToken);
}
