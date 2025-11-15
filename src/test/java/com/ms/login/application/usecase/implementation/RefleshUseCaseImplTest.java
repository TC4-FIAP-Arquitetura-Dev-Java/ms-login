package com.ms.login.application.usecase.implementation;

import com.ms.login.application.gateway.LoginGateway;
import com.ms.login.application.usecase.SessionTokenUseCase;
import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.domain.model.TokenInfoDomain;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import com.ms.login.mocks.LoginMock;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefleshUseCaseImplTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private LoginGateway loginGateway;

    @Mock
    private SessionTokenUseCase sessionTokenUseCase;

    @InjectMocks
    private RefleshUseCaseImpl refreshUseCase;

    private String refreshToken;
    private TokenInfoDomain tokenInfo;
    private LoginDomain loginDomain;

    @BeforeEach
    void setUp() {
        refreshToken = "refresh-token-123";
        tokenInfo = new TokenInfoDomain("1L", "username", LocalDateTime.now().plusDays(7));
        loginDomain = LoginMock.getLoginDomain();
    }

    @Test
    void shouldRefreshTokenSuccessfully() {
        // given
        String newAccessToken = "new-access-token";
        String newRefreshToken = "new-refresh-token";
        Date expiresAt = new Date(System.currentTimeMillis() + 3600000);
        Claims claims = createMockClaims();

        // when
        when(sessionTokenUseCase.validateRefreshToken(refreshToken)).thenReturn(tokenInfo);
        when(loginGateway.getUsername(tokenInfo.getUsername())).thenReturn(Optional.of(loginDomain));
        doNothing().when(sessionTokenUseCase).revokeRefreshToken(refreshToken);
        when(sessionTokenUseCase.generateRefreshToken(tokenInfo.getUserId(), tokenInfo.getUsername()))
                .thenReturn(newRefreshToken);
        when(jwtTokenProvider.generateAccessToken(anyString(), anyString(), any(RoleEnum.class)))
                .thenReturn(newAccessToken);
        when(jwtTokenProvider.extractClaimsAcessToken(newAccessToken)).thenReturn(claims);
        when(jwtTokenProvider.extractExpirationDate(claims)).thenReturn(expiresAt);

        AuthTokenDomain result = refreshUseCase.refreshToken(refreshToken);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo(newAccessToken);
        assertThat(result.getRefreshToken()).isEqualTo(newRefreshToken);
        assertThat(result.getUsername()).isEqualTo(loginDomain.getUsername());
        assertThat(result.getUserId()).isEqualTo(loginDomain.getUserId());
        assertThat(result.getRoleEnum()).isEqualTo(loginDomain.getRoleEnum().name());

        verify(sessionTokenUseCase).validateRefreshToken(refreshToken);
        verify(loginGateway).getUsername(tokenInfo.getUsername());
        verify(sessionTokenUseCase).revokeRefreshToken(refreshToken);
        verify(sessionTokenUseCase).generateRefreshToken(tokenInfo.getUserId(), tokenInfo.getUsername());
        verify(jwtTokenProvider).generateAccessToken(loginDomain.getUsername(), loginDomain.getUserId(), loginDomain.getRoleEnum());
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenIsExpired() {
        // given
        when(sessionTokenUseCase.validateRefreshToken(refreshToken))
                .thenThrow(new ExpiredJwtException(null, null, "Token expired"));

        // when & then
        assertThrows(BadCredentialsException.class, () -> refreshUseCase.refreshToken(refreshToken));

        verify(sessionTokenUseCase).validateRefreshToken(refreshToken);
        verifyNoInteractions(loginGateway);
    }

    @Test
    void shouldThrowExceptionWhenUserNotFound() {
        // given
        when(sessionTokenUseCase.validateRefreshToken(refreshToken)).thenReturn(tokenInfo);
        when(loginGateway.getUsername(tokenInfo.getUsername())).thenReturn(Optional.empty());

        // when & then
        // O RefleshUseCaseImpl captura todas as exceções e transforma em BadCredentialsException
        assertThrows(BadCredentialsException.class, () -> refreshUseCase.refreshToken(refreshToken));

        verify(sessionTokenUseCase).validateRefreshToken(refreshToken);
        verify(loginGateway).getUsername(tokenInfo.getUsername());
        verify(sessionTokenUseCase, never()).revokeRefreshToken(anyString());
    }

    @Test
    void shouldThrowExceptionWhenRefreshTokenIsInvalid() {
        // given
        when(sessionTokenUseCase.validateRefreshToken(refreshToken))
                .thenThrow(new SecurityException("Invalid refresh token"));

        // when & then
        assertThrows(BadCredentialsException.class, () -> refreshUseCase.refreshToken(refreshToken));

        verify(sessionTokenUseCase).validateRefreshToken(refreshToken);
        verifyNoInteractions(loginGateway);
    }

    private Claims createMockClaims() {
        Claims claims = mock(Claims.class, org.mockito.Answers.RETURNS_DEEP_STUBS);
        lenient().when(claims.getSubject()).thenReturn("username");
        lenient().when(claims.get("userId", String.class)).thenReturn("1L");
        lenient().when(claims.get("role", String.class)).thenReturn(RoleEnum.ADMIN.name());
        lenient().when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 3600000));
        return claims;
    }
}

