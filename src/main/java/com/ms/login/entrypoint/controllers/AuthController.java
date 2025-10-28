package com.ms.login.entrypoint.controllers;

import com.ms.login.application.usecase.CreateLoginUseCase;
import com.ms.login.application.usecase.LoginUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.entrypoint.controllers.presenter.AuthPresenter;
import com.ms.loginDomain.AutenticaoApi;
import com.ms.loginDomain.gen.model.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/v1")
public class AuthController implements AutenticaoApi {

    //TODO TOMORROW:
    // [] Testar as duas parte register e auth
    // [] Terminar e validar toda a parte de segurança do login
    // [] Desenvolver o refresh e logout
    // [] Fazer os teste e perfumarias

    private final LoginUseCase loginUseCase;
    private final CreateLoginUseCase createLoginUseCase;

    public AuthController(LoginUseCase loginUseCase, CreateLoginUseCase createLoginUseCase) {
        this.loginUseCase = loginUseCase;
        this.createLoginUseCase = createLoginUseCase;
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
    public ResponseEntity<LogoutResponseDto> _logout(LogoutRequestDto logoutRequestDto) {
        return null;
    }

    @Override
    public ResponseEntity<LoginResponseDto> _refreshToken(RefreshTokenRequestDto refreshTokenRequestDto) {
        return null;
    }
}
