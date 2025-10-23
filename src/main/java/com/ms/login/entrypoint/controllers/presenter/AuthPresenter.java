package com.ms.login.entrypoint.controllers.presenter;

import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.entrypoint.controllers.mappers.AuthDtoMapper;
import com.ms.loginDomain.gen.model.LoginResponseDto;

public class AuthPresenter {

    public static LoginResponseDto toLoginResponse(AuthTokenDomain authTokenDomain){
        return AuthDtoMapper.INSTANCE.toLoginResponse(authTokenDomain);
    }
}
