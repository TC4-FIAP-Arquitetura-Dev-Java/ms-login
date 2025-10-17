package com.ms.login.entrypoint.controllers.client;

import com.ms.login.application.gateway.UserClient;
import com.ms.login.entrypoint.controllers.client.dto.UserRequest;
import com.ms.login.entrypoint.controllers.client.dto.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserClientImpl implements UserClient {

    private final UserFeightClient userFeightClient;

    public UserClientImpl(UserFeightClient userFeightClient) {
        this.userFeightClient = userFeightClient;
    }

    @Override
    public UserResponse loadUserByUsername(String username) {
        return userFeightClient.loadUserByUsername(username);
    }

    @Override
    public UserResponse createUser(UserRequest request) {
        return userFeightClient.createUser(request);
    }
}
