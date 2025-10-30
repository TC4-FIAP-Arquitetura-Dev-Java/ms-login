package com.ms.login.application.usecase;

import com.ms.login.domain.model.LoginDomain;

public interface CreateLoginUseCase {

    void register(LoginDomain loginDomain);
}
