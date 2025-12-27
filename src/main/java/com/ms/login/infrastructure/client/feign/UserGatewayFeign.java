package com.ms.login.infrastructure.client.feign;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.dto.UserResponse;
import com.ms.login.infrastructure.client.mapper.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserGatewayFeign implements UserGateway {

    private final UserClientFeign UserClientFeign;

    public UserGatewayFeign(UserClientFeign client) {
        this.UserClientFeign = client;
    }

    @Override
    public void createUser(UserRequest request) {
        UserClientFeign.create(request);
    }

    @Override
    public Optional<UserDomain> getUserByUsername(String username) {
        UserResponse response = UserClientFeign.getUserByUsername(username);
        return Optional.of(UserMapper.INSTANCE.toUserDomain(response));
    }
}
