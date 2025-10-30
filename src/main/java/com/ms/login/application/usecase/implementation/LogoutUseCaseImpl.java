package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.LogoutUseCase;
import com.ms.login.application.usecase.SessionTokenUseCase;

public class LogoutUseCaseImpl implements LogoutUseCase {

    private final SessionTokenUseCase sessionTokenUseCase;

    public LogoutUseCaseImpl(SessionTokenUseCase sessionTokenUseCase) {
        this.sessionTokenUseCase = sessionTokenUseCase;
    }

    @Override
    public void logoutLogin(String refreshToken) {
        sessionTokenUseCase.revokeRefreshToken(refreshToken);
    }
}

