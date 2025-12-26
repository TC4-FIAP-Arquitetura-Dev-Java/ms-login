package com.ms.login.application.usecase;

import com.ms.login.domain.model.UserDomain;

import java.util.Optional;

public interface GetUserForLoginUseCase {

    Optional<UserDomain> execute(String username);
}
