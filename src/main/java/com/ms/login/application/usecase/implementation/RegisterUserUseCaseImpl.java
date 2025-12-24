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

    //    private final LoginGateway loginGateway;
//    private final LoginDomainService loginDomainService;
//    private final SecretKeyGenerator secretKeyGenerator;
//
//    public RegisterUserUseCaseImpl(LoginGateway loginGateway,
//                                   LoginDomainService loginDomainService,
//                                   SecretKeyGenerator secretKeyGenerator) {
//        this.loginGateway = loginGateway;
//        this.loginDomainService = loginDomainService;
//        this.secretKeyGenerator = secretKeyGenerator;
//    }
//
//    @Override
//    public void register(LoginDomain loginDomain) {
//        RequiredFieldsRule.checkRequiredFields(loginDomain);
//        loginDomainService.checkExistsUsername(loginDomain.getUsername());
//        loginDomain.setUsername(loginDomain.getUsername().toLowerCase());
//        loginDomain.setPassword(secretKeyGenerator.encode(loginDomain.getPassword()));
//        loginGateway.register(loginDomain);
//    }
}
