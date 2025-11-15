package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.database.entities.LoginDocument;
import com.ms.login.infrastructure.database.repositories.LoginRepository;
import com.ms.login.infrastructure.security.MyUserDetails;
import com.ms.login.infrastructure.security.MyUserDetailsService;
import com.ms.login.mocks.LoginMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        // Setup básico
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails_whenUserExists() {
        LoginDocument loginDocument = LoginMock.getLoginDocument();
        when(loginRepository.findByUsername("username")).thenReturn(loginDocument);

        UserDetails userDetails = userDetailsService.loadUserByUsername("username");

        assertThat(userDetails).isNotNull();
        assertThat(userDetails).isInstanceOf(MyUserDetails.class);
        assertThat(userDetails.getUsername()).isEqualTo("username");
        assertThat(userDetails.getPassword()).isEqualTo("password");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserNotFound() {
        when(loginRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("nonexistent"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("User not found");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenRepositoryThrowsException() {
        when(loginRepository.findByUsername("username")).thenThrow(new RuntimeException("Database error"));

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("username"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("Failed to find user credentials");
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenRepositoryThrowsNullPointerException() {
        when(loginRepository.findByUsername("username")).thenThrow(new NullPointerException("Null error"));

        assertThatThrownBy(() -> userDetailsService.loadUserByUsername("username"))
            .isInstanceOf(UsernameNotFoundException.class)
            .hasMessageContaining("Failed to find user credentials");
    }
}

