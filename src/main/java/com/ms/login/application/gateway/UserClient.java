package com.ms.login.application.gateway;

import com.ms.login.entrypoint.controllers.client.dto.UserRequest;
import com.ms.login.entrypoint.controllers.client.dto.UserResponse;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface UserClient {

    UserResponse loadUserByUsername(@PathVariable String username);

    UserResponse createUser(@RequestBody UserRequest request);
}
