package com.ms.login.application.usecase;

import com.ms.login.infrastructure.client.dto.UserRequest;

public interface RegisterUserUseCase {

    void register(UserRequest userDomain);
}
