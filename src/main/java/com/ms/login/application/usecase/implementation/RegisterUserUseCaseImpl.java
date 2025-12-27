package com.ms.login.application.usecase.implementation;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.application.usecase.RegisterUserUseCase;
import com.ms.login.infrastructure.client.dto.UserRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.ms.login.domain.rules.RequiredFieldsRule.checkRequiredFields;


public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserGateway userGateway;

    private static final Logger log = LoggerFactory.getLogger(RegisterUserUseCaseImpl.class);

    public RegisterUserUseCaseImpl(UserGateway userGateway) {
        this.userGateway = userGateway;
    }

    @Override
    public void register(UserRequest request) {
        checkRequiredFields(request);
        userGateway.createUser(request);
    }
}
