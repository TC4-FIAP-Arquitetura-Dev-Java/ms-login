package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.security.MyUserDetails;
import com.ms.login.infrastructure.security.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {}

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        UserDomain userDomain = new UserDomain(
                "1",
                "User Name",
                "username",     // <-- agora bate com o expected do teste
                "password",
                "email@test.com",
                true,
                RoleEnum.ADMIN
        );

        when(userGateway.getUserByUsername("username"))
                .thenReturn(Optional.of(userDomain));

        UserDetails userDetails = userDetailsService.loadUserByUsername("username");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(MyUserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo("username");
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(userGateway.getUserByUsername("nonexistent"))
                .thenReturn(Optional.empty()); // <-- CORREÇÃO AQUI

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nonexistent"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("User not found"); // agora passa
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenRepositoryThrowsException() {
        when(userGateway.getUserByUsername("username"))
                .thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Failed to find user credentials");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenRepositoryThrowsNullPointerException() {
        when(userGateway.getUserByUsername("username"))
                .thenThrow(new NullPointerException("Null error"));

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("username"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Failed to find user credentials");
    }
}

