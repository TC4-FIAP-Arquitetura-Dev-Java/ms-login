package com.ms.login.entrypoint.controllers;

import com.fasterxml.jackson.databind.DatabindException;
import com.ms.login.application.usecase.CreateLoginUseCase;
import com.ms.login.application.usecase.LoginUseCase;
import com.ms.login.application.usecase.LogoutUseCase;
import com.ms.login.application.usecase.RefreshTokenUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.entrypoint.controllers.presenter.AuthPresenter;
import com.ms.loginDomain.AutenticaoApi;
import com.ms.loginDomain.gen.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;

@RestController
@RequestMapping("/v1")
public class AuthController implements AutenticaoApi {

    private final LoginUseCase loginUseCase;
    private final CreateLoginUseCase createLoginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    public AuthController(LoginUseCase loginUseCase,
                          CreateLoginUseCase createLoginUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          LogoutUseCase logoutUseCase) {
        this.loginUseCase = loginUseCase;
        this.createLoginUseCase = createLoginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUseCase = logoutUseCase;
    }

    @Override
    public ResponseEntity<RegisterResponseDto> _register(RegisterRequestDto registerRequestDto) {
        LoginDomain loginDomain = AuthPresenter.toLoginDomain(registerRequestDto);
        createLoginUseCase.register(loginDomain);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Override
    public ResponseEntity<LoginResponseDto> _login(LoginRequestDto loginRequest) {
        AuthTokenDomain authToken = loginUseCase.login(loginRequest.getUsername(), loginRequest.getPassword());
        LoginResponseDto response = AuthPresenter.toLoginResponse(authToken);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<RefreshResponseDto> _refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        AuthTokenDomain refreshToken = refreshTokenUseCase.refreshToken(refreshTokenRequestDto.getRefreshToken());
        RefreshResponseDto response = AuthPresenter.toRefreshResponse(refreshToken);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<LogoutResponseDto> _logout(LogoutRequestDto logoutRequestDto) {
        logoutUseCase.logoutLogin(logoutRequestDto.getRefreshToken());
        LogoutResponseDto response = new LogoutResponseDto();
        response.setMessage("Refresh token revoked successfully");
        response.setTimestamp(OffsetDateTime.now());
        return ResponseEntity.ok(response);
    }
}
