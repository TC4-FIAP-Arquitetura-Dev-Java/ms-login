package com.ms.login.entrypoint.controllers;

import com.ms.login.application.usecase.LoginUseCase;
import com.ms.login.application.usecase.LogoutUseCase;
import com.ms.login.application.usecase.RefreshTokenUseCase;
import com.ms.login.application.usecase.RegisterUserUseCase;
import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.mapper.UserMapper;
import com.ms.login.mocks.UserMock;
import com.ms.loginDomain.gen.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.ResponseEntity;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    private LoginUseCase loginUseCase;
    private RegisterUserUseCase registerUserUseCase;
    private RefreshTokenUseCase refreshTokenUseCase;
    private LogoutUseCase logoutUseCase;
    private AuthController controller;
    private UserMapper userMapper;

    @BeforeEach
    void setup() {
        loginUseCase = mock(LoginUseCase.class);
        registerUserUseCase = mock(RegisterUserUseCase.class);
        refreshTokenUseCase = mock(RefreshTokenUseCase.class);
        logoutUseCase = mock(LogoutUseCase.class);
        userMapper = mock(UserMapper.class);

        controller = new AuthController(loginUseCase, registerUserUseCase, refreshTokenUseCase, logoutUseCase, userMapper);
    }

    // -------------------- REGISTER --------------------
    @Test
    void testRegister_success() {
        RegisterRequestDto request = new RegisterRequestDto();
        request.setUsername("usernameTest");
        request.setPassword("passwordTest");

        UserRequest mappedUserRequest = UserMock.getUserRequest();

        // Mock do mapper
        when(userMapper.toUserRequestDto(any(RegisterRequestDto.class)))
                .thenReturn(mappedUserRequest);

        // register é void, então usamos doNothing()
        doNothing().when(registerUserUseCase).register(any(UserRequest.class));

        ResponseEntity<RegisterResponseDto> response = controller._register(request);

        // Verificações
        assertEquals(201, response.getStatusCodeValue());
        verify(registerUserUseCase, times(1)).register(mappedUserRequest);

        // Verifica argumentos usando ArgumentCaptor
        ArgumentCaptor<UserRequest> captor = ArgumentCaptor.forClass(UserRequest.class);
        verify(registerUserUseCase).register(captor.capture());
        assertEquals("usernameTest", captor.getValue().username());
        assertEquals("passwordTest", captor.getValue().password());
    }

    // -------------------- LOGIN --------------------
    @Test
    void testLogin_success() {
        LoginRequestDto request = new LoginRequestDto();
        request.setUsername("gustavo");
        request.setPassword("123");

        AuthTokenDomain token = new AuthTokenDomain("token123", "refresh123", "ADMIN", "sdf5dsf4", OffsetDateTime.now().toString());
        when(loginUseCase.login("gustavo","123")).thenReturn(token);

        ResponseEntity<LoginResponseDto> response = controller._login(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("token123", response.getBody().getAccessToken());
        verify(loginUseCase, times(1)).login("gustavo","123");
    }

    // -------------------- REFRESH TOKEN --------------------
    @Test
    void testRefreshToken_success() {
        RefreshTokenRequestDto request = new RefreshTokenRequestDto();
        request.setRefreshToken("refresh123");

        AuthTokenDomain token = new AuthTokenDomain("newToken", "refresh123", "ADMIN", "sdf5dsf4", OffsetDateTime.now().toString());
        when(refreshTokenUseCase.refreshToken("refresh123")).thenReturn(token);

        ResponseEntity<RefreshResponseDto> response = controller._refreshToken(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("newToken", response.getBody().getAccessToken());
        verify(refreshTokenUseCase, times(1)).refreshToken("refresh123");
    }

    // -------------------- LOGOUT --------------------
    @Test
    void testLogout_success() {
        LogoutRequestDto request = new LogoutRequestDto();
        request.setRefreshToken("logoutToken");

        doNothing().when(logoutUseCase).logoutLogin("logoutToken");

        ResponseEntity<LogoutResponseDto> response = controller._logout(request);

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals("Refresh token revoked successfully", response.getBody().getMessage());
        verify(logoutUseCase, times(1)).logoutLogin("logoutToken");
    }
}
