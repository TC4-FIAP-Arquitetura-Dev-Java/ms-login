package com.ms.login.application.usecase.implementation;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.mocks.UserMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterUserUseCaseImplTest {

    @Mock
    private UserGateway userGateway;

    @InjectMocks
    private RegisterUserUseCaseImpl registerUserUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCallGatewaySuccessfully() {
        UserRequest user = UserMock.getUserRequest();

        doNothing().when(userGateway).createUser(any(UserRequest.class));
        registerUserUseCase.register(user);

        verify(userGateway, times(1)).createUser(user);
    }

    @Test
    void shouldThrowExceptionWhenGatewayFails() {
        UserRequest user = UserMock.getUserRequest();

        doThrow(new RuntimeException("Database error"))
                .when(userGateway).createUser(user);

        // Verifica se o register lança a exceção
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registerUserUseCase.register(user));

        // Verifica a mensagem da exceção
        assert(exception.getMessage().equals("Database error"));

        verify(userGateway, times(1)).createUser(user);
    }

    @Test
    void shouldNotCallGatewayWithNull() {
        UserRequest user = null;

        doThrow(new NullPointerException("User cannot be null"))
                .when(userGateway).createUser(user);

        assertThrows(NullPointerException.class,
                () -> registerUserUseCase.register(user));

        verify(userGateway, times(1)).createUser(user);
    }
}