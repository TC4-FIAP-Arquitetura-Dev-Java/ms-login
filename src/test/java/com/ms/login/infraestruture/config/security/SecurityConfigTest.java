package com.ms.login.infraestruture.config.security;

import com.ms.login.infrastructure.security.JwtAuthenticationFilter;
import com.ms.login.infrastructure.security.RateLimitingFilter;
import com.ms.login.infrastructure.security.SecurityConfig;
import com.ms.login.infrastructure.security.SecurityHeadersFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SecurityConfigTest {

    @Mock
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private RateLimitingFilter rateLimitingFilter;

    @Mock
    private SecurityHeadersFilter securityHeadersFilter;

    @Mock
    private AuthenticationConfiguration authenticationConfiguration;

    @InjectMocks
    private SecurityConfig securityConfig;

    @Test
    void passwordEncoder_shouldReturnBCryptPasswordEncoder() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();

        assertThat(encoder).isNotNull();
        assertThat(encoder).isInstanceOf(org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder.class);
    }

    @Test
    void authenticationManager_shouldReturnAuthenticationManager() throws Exception {
        AuthenticationManager authManager = mock(AuthenticationManager.class);
        when(authenticationConfiguration.getAuthenticationManager()).thenReturn(authManager);

        AuthenticationManager result = securityConfig.authenticationManager(authenticationConfiguration);

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(authManager);
        verify(authenticationConfiguration).getAuthenticationManager();
    }
}

