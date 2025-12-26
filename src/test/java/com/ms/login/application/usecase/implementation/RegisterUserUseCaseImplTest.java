package com.ms.login.application.usecase.implementation;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.domain.model.UserDomain;
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
    void shouldRegisterUserSuccessfully() {

        UserDomain user = UserMock.getUserDomain();

        when(userGateway.createUser(any(UserDomain.class))).thenReturn(user);

        UserDomain result = registerUserUseCase.register(user);

        assertNotNull(result);
        assertEquals("usernameTest", result.getUsername());
        assertEquals("user@test.com", result.getEmail());
        assertEquals("passwordTest", result.getPassword());

        verify(userGateway, times(1)).createUser(any(UserDomain.class));
    }

    @Test
    void shouldThrowExceptionWhenGatewayFails() {
        UserDomain user = UserMock.getUserDomain();

        when(userGateway.createUser(user))
                .thenThrow(new RuntimeException("Database error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> registerUserUseCase.register(user));

        assertEquals("Database error", exception.getMessage());
        verify(userGateway, times(1)).createUser(user);
    }

    @Test
    void shouldNotReturnNull() {
        UserDomain user = UserMock.getUserDomain();

        when(userGateway.createUser(any(UserDomain.class))).thenReturn(user);

        UserDomain result = registerUserUseCase.register(user);

        assertNotNull(result);
        verify(userGateway).createUser(user);
    }
}