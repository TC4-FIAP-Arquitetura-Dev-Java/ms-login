package com.ms.login.infrastructure.config;

import com.ms.login.application.gateway.UserClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService {

    private final UserClient usuarioClient;

    public CustomUserDetailsService(UserClient usuarioClient) {
        this.usuarioClient = usuarioClient;
    }

    //    TODO: FAZER FEIGN COM O MS-USUARIO PARA PEGAR O DADOS DE LA
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
