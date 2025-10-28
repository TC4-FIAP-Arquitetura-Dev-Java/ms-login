package com.ms.login.infrastructure.database.implementations;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.infrastructure.database.entities.LoginDocument;
import com.ms.login.infrastructure.database.mappers.LoginDocumentMapper;
import com.ms.login.infrastructure.database.repositories.LoginRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LoginGatewayImpl implements LoginGateway {

    private final LoginRepository loginRepository;

    @Override
    public void register(LoginDomain loginDomain) {
        LoginDocument loginDocument = LoginDocumentMapper.INSTANCE.toDocument(loginDomain);
        loginRepository.save(loginDocument);
    }

    @Override
    public Optional<LoginDomain> getUsername(String username) {
        LoginDocument loginDocument = loginRepository.findByUsername(username);

        if(loginDocument != null){
            return Optional.of(LoginDocumentMapper.INSTANCE.toDomain(loginDocument));
        }
        return Optional.empty();
    }
}
