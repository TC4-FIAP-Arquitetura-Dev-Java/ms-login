package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.infrastructure.database.entities.LoginDocument;
import com.ms.login.infrastructure.security.MyUserDetails;
import com.ms.login.mocks.LoginMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

class MyUserDetailsTest {

    private LoginDocument loginDocument;

    @BeforeEach
    void setUp() {
        loginDocument = LoginMock.getLoginDocument();
    }

    @Test
    void constructorWithLoginDocument_shouldSetAllFields() {
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        assertThat(userDetails.getUserId()).isEqualTo(loginDocument.getUserId());
        assertThat(userDetails.getUsername()).isEqualTo(loginDocument.getUsername());
        assertThat(userDetails.getPassword()).isEqualTo(loginDocument.getPassword());
        assertThat(userDetails.getRoleEnum()).isEqualTo(loginDocument.getRoleEnum());
    }

    @Test
    void constructorWithLoginDocument_shouldCreateCorrectAuthority() {
        loginDocument.setRoleEnum(RoleEnum.ADMIN);
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_ADMIN");
    }

    @Test
    void constructorWithLoginDocument_shouldCreateUserAuthority() {
        loginDocument.setRoleEnum(RoleEnum.USER);
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    void constructorWithParameters_shouldSetAllFields() {
        String userId = "123";
        String username = "testuser";
        String password = "password123";
        RoleEnum role = RoleEnum.USER;
        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
            new SimpleGrantedAuthority("ROLE_USER")
        );

        MyUserDetails userDetails = new MyUserDetails(userId, username, password, role, authorities);

        assertThat(userDetails.getUserId()).isEqualTo(userId);
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getPassword()).isEqualTo(password);
        assertThat(userDetails.getRoleEnum()).isEqualTo(role);
        assertThat(userDetails.getAuthorities()).isEqualTo(authorities);
    }

    @Test
    void isAccountNonExpired_shouldReturnTrue() {
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        assertThat(userDetails.isAccountNonExpired()).isTrue();
    }

    @Test
    void isAccountNonLocked_shouldReturnTrue() {
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        assertThat(userDetails.isAccountNonLocked()).isTrue();
    }

    @Test
    void isCredentialsNonExpired_shouldReturnTrue() {
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        assertThat(userDetails.isCredentialsNonExpired()).isTrue();
    }

    @Test
    void isEnabled_shouldReturnTrue() {
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        assertThat(userDetails.isEnabled()).isTrue();
    }

    @Test
    void getRoleEnum_shouldReturnCorrectRole() {
        loginDocument.setRoleEnum(RoleEnum.ADMIN);
        MyUserDetails userDetails = new MyUserDetails(loginDocument);

        assertThat(userDetails.getRoleEnum()).isEqualTo(RoleEnum.ADMIN);
    }
}


