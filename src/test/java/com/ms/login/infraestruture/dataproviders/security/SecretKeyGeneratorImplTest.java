package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.infrastructure.security.password.SecretKeyGeneratorImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecretKeyGeneratorImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private SecretKeyGeneratorImpl secretKeyGenerator;

    @BeforeEach
    void setUp() {
        // Setup básico
    }

    @Test
    void encode_shouldCallPasswordEncoder() {
        String rawPassword = "plainPassword123";
        String encodedPassword = "$2a$10$encodedPasswordHash";

        when(passwordEncoder.encode(rawPassword)).thenReturn(encodedPassword);

        String result = secretKeyGenerator.encode(rawPassword);

        assertThat(result).isEqualTo(encodedPassword);
        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void encode_shouldHandleDifferentPasswords() {
        String password1 = "password1";
        String password2 = "password2";
        String encoded1 = "$2a$10$hash1";
        String encoded2 = "$2a$10$hash2";

        when(passwordEncoder.encode(password1)).thenReturn(encoded1);
        when(passwordEncoder.encode(password2)).thenReturn(encoded2);

        assertThat(secretKeyGenerator.encode(password1)).isEqualTo(encoded1);
        assertThat(secretKeyGenerator.encode(password2)).isEqualTo(encoded2);
    }
}

