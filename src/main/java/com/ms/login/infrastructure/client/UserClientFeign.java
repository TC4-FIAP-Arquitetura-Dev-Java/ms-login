package com.ms.login.infrastructure.client;

import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "ms-usuario", url = "${services.ms-usuario.url}")
public interface UserClientFeign {

    @PostMapping("/users")
    UserResponse create(@RequestBody UserRequest userRequest);

    @GetMapping("users/username/{username}")
    UserResponse getUserByUsername(@PathVariable("username") String username);
}
