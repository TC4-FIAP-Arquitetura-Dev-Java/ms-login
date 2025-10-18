package com.ms.login.entrypoint.controllers.client;

import com.ms.login.entrypoint.controllers.client.dto.UserRequest;
import com.ms.login.entrypoint.controllers.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "usuario-service", url = "${usuario.service.url}")
public interface UserFeightClient {

    @GetMapping("v1/usuarios/getUser/{username}")
    UserResponse loadUserByUsername(@PathVariable("username") String username) throws UsernameNotFoundException;

    @PostMapping("v1/usuarios")
    UserResponse createUser(@RequestBody UserRequest request);
}
