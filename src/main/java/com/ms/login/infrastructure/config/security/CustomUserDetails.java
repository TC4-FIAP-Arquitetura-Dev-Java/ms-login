package com.ms.login.infrastructure.config.security;

import com.ms.login.entrypoint.controllers.client.dto.UserResponse;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

public class CustomUserDetails implements UserDetails{

    //Classe responsavel por estruturar o objeto usuario para ser utilizado na autenticacao

    private String id;
    private String usuario;
    private String nome;
    private String email;
    private String password;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomUserDetails(UserResponse usuario) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return usuario;
    }
}
