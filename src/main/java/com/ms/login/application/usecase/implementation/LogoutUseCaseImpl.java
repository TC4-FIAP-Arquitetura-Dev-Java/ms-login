package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.LogoutUseCase;
import com.ms.login.infrastructure.config.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Date;

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

