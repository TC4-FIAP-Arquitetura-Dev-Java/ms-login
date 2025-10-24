package com.ms.login.infrastructure.database.implementations;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.infrastructure.database.entities.LoginDocument;
import com.ms.login.infrastructure.database.mappers.LoginDocumentMapper;
import com.ms.login.infrastructure.database.repositories.LoginRepository;

import java.util.Optional;

public class LoginGatewayImpl implements LoginGateway {

    private final LoginRepository loginRepository;

    public LoginGatewayImpl(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    @Override
    public void register(LoginDomain loginDomain) {
        LoginDocument loginDocument = LoginDocumentMapper.INSTANCE.toDocument(loginDomain);
        loginRepository.save(loginDocument);
    }

//    @Override
//    public Optional<LoginDomain> authenticate(CredentialDomain credentials) {
//        Optional<LoginDocument> document = loginRepository.findById(credentials.getUsername());
//        LoginDomain loginDomain = LoginDocumentMapper.INSTANCE.toDomain(document.get());
//        return Optional.of(loginDomain);
//    }
//
//    @Override
//    public AuthTokenDomain refreshToken(String refreshToken) {
//        return null;
//    }

//    @Override
//    public void invalidateTokens(String userId) {}


    @Override
    public Optional<LoginDomain> getUsername(String username) {
        return Optional.empty();
    }
}
