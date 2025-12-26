package com.ms.login.application.port.out;

import com.ms.login.domain.model.UserDomain;
import java.util.Optional;

public interface UserGateway {

    UserDomain createUser(UserDomain user);

    Optional<UserDomain> getUserByUsername(String username);
}
