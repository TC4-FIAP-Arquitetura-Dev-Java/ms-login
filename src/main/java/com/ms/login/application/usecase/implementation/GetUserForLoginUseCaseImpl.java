package com.ms.login.application.usecase.implementation;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.application.usecase.GetUserForLoginUseCase;
import com.ms.login.domain.model.UserDomain;

import java.util.Optional;

public class GetUserForLoginUseCaseImpl implements GetUserForLoginUseCase {

    private final UserGateway userGateway;

    public GetUserForLoginUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public Optional<UserDomain> execute(String username) {
        return userGateway.getUserByUsername(username);
    }
}
