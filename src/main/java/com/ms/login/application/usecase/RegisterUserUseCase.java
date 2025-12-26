package com.ms.login.application.usecase;

import com.ms.login.domain.model.UserDomain;

public interface RegisterUserUseCase {

    UserDomain register(UserDomain userDomain);
}
