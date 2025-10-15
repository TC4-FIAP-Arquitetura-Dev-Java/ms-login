package com.ms.login.entrypoint.controllers.client;

import com.ms.login.entrypoint.controllers.client.dto.UserRequest;
import com.ms.login.entrypoint.controllers.client.dto.UserResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "usuario-service", url = "${MS-USUARIO_ENDPOINT}")
public interface UserFeightClient {

    //Como estou pegando dados de outros ms devo fornecer todos os dtos necessarios para usar os dados da forma correta

    //Ao realizar um openFeight eu devo buscar por user name para verificar se aquele determinado usuario existe antes de realizar um operacao

    @GetMapping("v1/user/{username}")
    UserResponse loadUserByUsername(@PathVariable("username") String username) throws UsernameNotFoundException;

    @PostMapping("v1/user")
    UserResponse createUser(@RequestBody UserRequest request);
}
