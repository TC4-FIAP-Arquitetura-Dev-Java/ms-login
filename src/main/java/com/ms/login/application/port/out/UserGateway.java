package com.ms.login.application.port.out;

import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.UserRequest;

import java.util.Optional;

public interface UserGateway {

    void createUser(UserRequest user);

    Optional<UserDomain> getUserByUsername(String username);
}
