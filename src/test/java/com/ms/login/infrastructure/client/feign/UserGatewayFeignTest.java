package com.ms.login.infrastructure.client.feign;

import com.ms.login.application.port.out.UserGateway;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.dto.UserResponse;
import com.ms.login.mocks.UserMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserGatewayFeignTest {

    private UserClientFeign client;
    private UserGateway gateway;

    @BeforeEach
    void setup() {
        client = mock(UserClientFeign.class);
        gateway = new UserGatewayFeign(client);
    }

    @Test
    void testCreateUser_success() {
        // Arrange
        UserRequest userRequest = UserMock.getUserRequest();
        UserResponse userResponse = UserMock.getUserResponse();

        // Mock do client para retornar UserResponse
        when(client.create(any(UserRequest.class))).thenReturn(userResponse);

        // Método do gateway é void, então usamos doNothing()
        doNothing().when(gateway).createUser(any(UserRequest.class));

        // Act
        gateway.createUser(userRequest); // Método void

        // Assert
        ArgumentCaptor<UserRequest> captor = ArgumentCaptor.forClass(UserRequest.class);
        verify(client, times(1)).create(captor.capture());
        verify(gateway, times(1)).createUser(any(UserRequest.class));

        // Verifica valores enviados ao client
        UserRequest captured = captor.getValue();
        assertEquals("usernameTest", captured.username());
        assertEquals("user@test.com", captured.email());
    }

    @Test
    void testGetUserByUsername_success() {
        when(client.getUserByUsername("usernameTest"))
                .thenReturn(UserMock.getUserResponse());

        Optional<UserDomain> result = gateway.getUserByUsername("usernameTest");

        assertTrue(result.isPresent());
        assertEquals("usernameTest", result.get().getUsername());
        verify(client, times(1)).getUserByUsername("usernameTest");
    }

    @Test
    void testGetUserByUsername_notFound_throwsException() {
        when(client.getUserByUsername("notfound")).thenReturn(null);

        assertThrows(NullPointerException.class,
                () -> gateway.getUserByUsername("notfound").get());

        verify(client, times(1)).getUserByUsername("notfound");
    }
}
