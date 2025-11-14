package com.ms.login.infraestruture.dataproviders.database;

import com.ms.login.domain.model.LoginDomain;
import com.ms.login.infrastructure.database.entities.LoginDocument;
import com.ms.login.infrastructure.database.implementations.LoginGatewayImpl;
import com.ms.login.infrastructure.database.repositories.LoginRepository;
import com.ms.login.mocks.LoginMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LoginGatewayImplTest {

    @Mock
    private LoginRepository loginRepository;

    @InjectMocks
    private LoginGatewayImpl loginGateway;

    @BeforeEach
    void setUp() {
        // Setup básico
    }

    @Test
    void register_shouldSaveLoginDocument() {
        LoginDomain loginDomain = LoginMock.getLoginDomain();
        LoginDocument loginDocument = LoginMock.getLoginDocument();

        when(loginRepository.save(any(LoginDocument.class))).thenReturn(loginDocument);

        loginGateway.register(loginDomain);

        verify(loginRepository).save(any(LoginDocument.class));
    }

    @Test
    void getUsername_shouldReturnOptional_whenUserExists() {
        LoginDocument loginDocument = LoginMock.getLoginDocument();
        when(loginRepository.findByUsername("username")).thenReturn(loginDocument);

        Optional<LoginDomain> result = loginGateway.getUsername("username");

        assertThat(result).isPresent();
        assertThat(result.get().getUsername()).isEqualTo("username");
        verify(loginRepository).findByUsername("username");
    }

    @Test
    void getUsername_shouldReturnEmpty_whenUserNotFound() {
        when(loginRepository.findByUsername("nonexistent")).thenReturn(null);

        Optional<LoginDomain> result = loginGateway.getUsername("nonexistent");

        assertThat(result).isEmpty();
        verify(loginRepository).findByUsername("nonexistent");
    }
}

