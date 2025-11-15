package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.SessionTokenUseCase;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LogoutUseCaseImplTest {

    @Mock
    private SessionTokenUseCase sessionTokenUseCase;

    @InjectMocks
    private LogoutUseCaseImpl logoutUseCase;

    @Test
    void shouldLogoutSuccessfully() {
        // given
        String refreshToken = "refresh-token-123";
        com.ms.login.domain.model.TokenInfoDomain tokenInfo = 
            new com.ms.login.domain.model.TokenInfoDomain("1L", "username", java.time.LocalDateTime.now().plusDays(7));

        // when
        when(sessionTokenUseCase.validateRefreshToken(refreshToken)).thenReturn(tokenInfo);
        doNothing().when(sessionTokenUseCase).revokeRefreshToken(refreshToken);

        logoutUseCase.logoutLogin(refreshToken);

        // then
        verify(sessionTokenUseCase).validateRefreshToken(refreshToken);
        verify(sessionTokenUseCase).revokeRefreshToken(refreshToken);
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenIsInvalid() {
        // given
        String invalidRefreshToken = "invalid-token";

        // when
        doThrow(new SecurityException("Invalid refresh token"))
                .when(sessionTokenUseCase)
                .validateRefreshToken(invalidRefreshToken);

        // then
        org.junit.jupiter.api.Assertions.assertThrows(SecurityException.class,
                () -> logoutUseCase.logoutLogin(invalidRefreshToken));

        verify(sessionTokenUseCase).validateRefreshToken(invalidRefreshToken);
        verify(sessionTokenUseCase, never()).revokeRefreshToken(anyString());
    }
}

