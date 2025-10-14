package com.ms.login.entrypoint.controllers.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "usuario-service", url = "${MS-USUARIO_ENDPOINT}")
public interface UserFeightClient {

    @GetMapping("v1/user/{id}")
    UserDetails loadUserByUsername(@PathVariable Long id) throws UsernameNotFoundException;

    @PostMapping("v1/user")
    UserDetails createUser(@RequestBody UserDetails userDetails);

}
