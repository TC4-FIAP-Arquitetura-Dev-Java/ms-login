package com.ms.login.infrastructure.config.security;

import com.ms.login.application.gateway.UserClient;
import com.ms.login.entrypoint.controllers.client.dto.UserResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient usuarioClient;

    public CustomUserDetailsService(UserClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    //Busca o usuario e mosta o CustomUserDetails para ser utilizado dentro do autenticationManager
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try{
            UserResponse usuario = usuarioClient.loadUserByUsername(username);

            if(usuario == null){
                throw new UsernameNotFoundException("Usuário não encontrado");
            }
            return new CustomUserDetails(usuario);
        }catch (Exception e){
            throw new UsernameNotFoundException("Erro ao buscar usuario via Feign: " + e.getMessage());
        }
    }
}
