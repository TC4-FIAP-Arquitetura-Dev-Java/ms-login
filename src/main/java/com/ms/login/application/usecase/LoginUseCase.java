package com.ms.login.application.usecase;

import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.CredentialDomain;

public interface LoginUseCase {

    AuthTokenDomain login(CredentialDomain credentialDomain);

}
