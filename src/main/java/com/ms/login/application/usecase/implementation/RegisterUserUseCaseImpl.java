package com.ms.login.application.usecase.implementation;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.application.usecase.RegisterUserUseCase;
import com.ms.login.domain.model.UserDomain;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserGateway userGateway;

    public RegisterUserUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public UserDomain register(UserDomain userDomain) {
        return userGateway.createUser(userDomain);
    }
}
