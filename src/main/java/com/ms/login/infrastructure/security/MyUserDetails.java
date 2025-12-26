package com.ms.login.infrastructure.security;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.UserDomain;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class MyUserDetails implements UserDetails {

    private final String userId;
    private final String username;
    private final String password;
    private final RoleEnum roleEnum;

    private final Collection<? extends GrantedAuthority> authorities;

    public MyUserDetails(UserDomain usuario) {
        this.userId = usuario.getUserId();
        this.username = usuario.getUsername();
        this.password = usuario.getPassword();
        this.roleEnum = usuario.getRoleEnum();

        // Cria a autoridade com base no enum (ex: ROLE_ADMIN, ROLE_USER)
        this.authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + roleEnum.name())
        );
    }

    public MyUserDetails(String userId, String username, String password, RoleEnum roleEnum, Collection<? extends GrantedAuthority> authorities) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roleEnum = roleEnum;
        this.authorities = authorities;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // or your own logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // or your own logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // or your own logic
    }

    @Override
    public boolean isEnabled() {
        return true; // or your own logic
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}