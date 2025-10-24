package com.ms.login.entrypoint.controllers;

import com.ms.login.application.usecase.CreateLoginUseCase;
import com.ms.login.application.usecase.LoginUseCase;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.entrypoint.controllers.presenter.AuthPresenter;
import com.ms.loginDomain.AutenticaoApi;
import com.ms.loginDomain.gen.model.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/v1")
public class AuthController implements AutenticaoApi {

    //TODO TOMORROW:
    // [x] Criar a classe de config para reconher o bean.
    // [x] Criar um usuario qualquer no banco ,
    // [x] Fazer o primeiro teste se esta gerando o token
    // [x] Com toda essa parte de cima feita e funcionando, proximo passo e o refresh token
    // [] Desenvolver a parte de register
    // [] Resolver a parte dos beans
    // [] Testar as duas parte register e auth

    private final LoginUseCase loginUseCase;
    private final CreateLoginUseCase createLoginUseCase;

    public AuthController(LoginUseCase loginUseCase, CreateLoginUseCase createLoginUseCase) {
        this.loginUseCase = loginUseCase;
        this.createLoginUseCase = createLoginUseCase;
    }

    //    private final LogoutUseCase logoutUseCase;
//    private final CreateLoginUseCase  createLoginUseCase;
//
//    public AuthController(LoginUseCase loginUseCase,
//                          LogoutUseCase logoutUseCase,
//                          CreateLoginUseCase createLoginUseCase) {
//        this.loginUseCase = loginUseCase;
//        this.logoutUseCase = logoutUseCase;
//        this.createLoginUseCase = createLoginUseCase;
//    }

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

    @Override
    public ResponseEntity<RegisterResponseDto> _register(RegisterRequestDto registerRequestDto) {
        return null;
    }
}
