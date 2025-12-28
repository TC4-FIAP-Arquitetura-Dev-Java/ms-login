package com.ms.login.infrastructure.client.feign;

import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.dto.UserResponse;
import com.ms.login.mocks.UserMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserGatewayFeignTest {

    @Mock
    private UserClientFeign client; // Feign client deve ser mock

    @InjectMocks
    private UserGatewayFeign gateway; // Classe real que queremos testar

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_success() {
        // Arrange
        UserRequest userRequest = UserMock.getUserRequest();
        UserResponse userResponse = UserMock.getUserResponse();

        when(client.create(any(UserRequest.class))).thenReturn(userResponse);

        // Act
        gateway.createUser(userRequest);

        // Assert
        ArgumentCaptor<UserRequest> captor = ArgumentCaptor.forClass(UserRequest.class);
        verify(client, times(1)).create(captor.capture()); // verifica chamada no feign

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
