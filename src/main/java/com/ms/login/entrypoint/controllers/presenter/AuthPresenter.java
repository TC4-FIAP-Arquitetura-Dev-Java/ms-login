package com.ms.login.entrypoint.controllers.presenter;

import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.entrypoint.controllers.mappers.AuthDtoMapper;
import com.ms.loginDomain.gen.model.LoginResponseDto;
import com.ms.loginDomain.gen.model.RefreshResponseDto;
import com.ms.loginDomain.gen.model.RegisterRequestDto;

public class AuthPresenter {

    public static LoginResponseDto toLoginResponse(AuthTokenDomain authTokenDomain){
        return AuthDtoMapper.INSTANCE.toLoginResponseDto(authTokenDomain);
    }

    public static RefreshResponseDto toRefreshResponse(AuthTokenDomain authTokenDomain){
        return AuthDtoMapper.INSTANCE.toRefreshResponseDto(authTokenDomain);
    }

    public static LoginDomain toLoginDomain(RegisterRequestDto registerRequestDto) {
        return AuthDtoMapper.INSTANCE.toLoginDomain(registerRequestDto);
    }
}
