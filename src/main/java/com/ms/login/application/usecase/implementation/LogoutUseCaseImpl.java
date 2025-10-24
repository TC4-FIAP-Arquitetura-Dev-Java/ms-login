package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.LogoutUseCase;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import org.springframework.security.authentication.BadCredentialsException;

public class LogoutUseCaseImpl implements LogoutUseCase {

    private final JwtTokenProvider jwtTokenProvider;

    public LogoutUseCaseImpl(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void logoutLogin(String refreshToken) {
        try {
            if (!jwtTokenProvider.validateToken(refreshToken)) {
                throw new BadCredentialsException("Invalid refresh token");
            }

        } catch (Exception e) {
            throw new BadCredentialsException("Unable to logout");
        }
    }
}

