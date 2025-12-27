package com.ms.login.entrypoint.controllers;

import com.ms.login.application.usecase.RegisterUserUseCase;
import com.ms.login.application.usecase.LoginUseCase;
import com.ms.login.application.usecase.LogoutUseCase;
import com.ms.login.application.usecase.RefreshTokenUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.entrypoint.controllers.presenter.AuthPresenter;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.mapper.UserMapper;
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
    private final RegisterUserUseCase registerUserUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final LogoutUseCase logoutUseCase;

    private final UserMapper userMapper;

    public AuthController(LoginUseCase loginUseCase,
                          RegisterUserUseCase registerUserUseCase,
                          RefreshTokenUseCase refreshTokenUseCase,
                          LogoutUseCase logoutUseCase,
                          UserMapper userMapper) {
        this.loginUseCase = loginUseCase;
        this.registerUserUseCase = registerUserUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.logoutUseCase = logoutUseCase;
        this.userMapper = userMapper;
    }

    @Override
    public ResponseEntity<RegisterResponseDto> _register(RegisterRequestDto registerRequestDto) {
        UserRequest userRequest = userMapper.toUserRequestDto(registerRequestDto);
        registerUserUseCase.register(userRequest);
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
