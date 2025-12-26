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
        UserDomain userDomain = UserMock.getUserDomain();
        UserResponse response = UserMock.getUserResponse();

        when(client.create(any(UserRequest.class))).thenReturn(response);

        UserDomain result = gateway.createUser(userDomain);

        assertNotNull(result);
        assertEquals("usernameTest", result.getUsername());
        assertEquals("user@test.com", result.getEmail());

        ArgumentCaptor<UserRequest> captor = ArgumentCaptor.forClass(UserRequest.class);
        verify(client, times(1)).create(captor.capture());

        assertEquals("usernameTest", captor.getValue().username());
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
