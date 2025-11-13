package com.ms.login.application.usecase.implementation;

import com.ms.login.application.usecase.SessionTokenUseCase;
import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.infrastructure.security.*;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private SecurityAuditLogger securityAuditLogger;

    @Mock
    private RateLimitingFilter rateLimitingFilter;

    @Mock
    private ClientIpResolver clientIpResolver;

    @Mock
    private SessionTokenUseCase sessionTokenUseCase;

    @InjectMocks
    private LoginUseCaseImpl loginUseCase;

    private MyUserDetails userDetails;
    private String username;
    private String password;
    private String clientIp;

    @BeforeEach
    void setUp() {
        username = "testuser";
        password = "password123";
        clientIp = "192.168.1.1";

        userDetails = new MyUserDetails("1L", username, "encodedPassword", RoleEnum.USER, null);

        when(clientIpResolver.resolve()).thenReturn(clientIp);
    }

    @Test
    void shouldLoginSuccessfully() {
        // given
        Authentication authentication = new TestingAuthenticationToken(userDetails, null);
        String accessToken = "access-token";
        String refreshToken = "refresh-token";
        Date expiresAt = new Date(System.currentTimeMillis() + 3600000);
        Claims claims = createMockClaims();

        // when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(anyString(), anyString(), any(RoleEnum.class)))
                .thenReturn(accessToken);
        when(sessionTokenUseCase.generateRefreshToken(anyString(), anyString()))
                .thenReturn(refreshToken);
        when(jwtTokenProvider.extractClaimsAcessToken(accessToken)).thenReturn(claims);
        when(jwtTokenProvider.extractExpirationDate(claims)).thenReturn(expiresAt);

        AuthTokenDomain result = loginUseCase.login(username, password);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getAccessToken()).isEqualTo(accessToken);
        assertThat(result.getRefreshToken()).isEqualTo(refreshToken);
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getUserId()).isEqualTo("1L");
        assertThat(result.getRoleEnum()).isEqualTo(RoleEnum.USER.name());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateAccessToken(username, "1L", RoleEnum.USER);
        verify(sessionTokenUseCase).generateRefreshToken("1L", username);
        verify(securityAuditLogger).logLoginAttempt(username, clientIp, true, "SUCCESS");
        verify(rateLimitingFilter).clearAttempts(clientIp);
    }

    @Test
    void shouldThrowExceptionWhenInputContainsMaliciousContent() {
        // given
        String maliciousUsername = "admin' OR '1'='1";
        String maliciousPassword = "<script>alert('xss')</script>";

        // when & then
        assertThrows(BadCredentialsException.class, () -> loginUseCase.login(maliciousUsername, maliciousPassword));

        verify(securityAuditLogger).logSuspiciousActivity(maliciousUsername, clientIp, "MALICIOUS_INPUT",
                "Attempted login with malicious input");
        verifyNoInteractions(authenticationManager);
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void shouldThrowExceptionWhenCredentialsAreInvalid() {
        // given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // when & then
        assertThrows(BadCredentialsException.class, () -> loginUseCase.login(username, password));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(securityAuditLogger).logLoginAttempt(username, clientIp, false, "INVALID_CREDENTIALS");
        verify(rateLimitingFilter).recordFailedAttempt(clientIp);
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void shouldThrowExceptionWhenSystemErrorOccurs() {
        // given
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new RuntimeException("System error"));

        // when & then
        assertThrows(BadCredentialsException.class, () -> loginUseCase.login(username, password));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(securityAuditLogger).logLoginAttempt(username, clientIp, false, "SYSTEM_ERROR");
        verifyNoInteractions(jwtTokenProvider);
    }

    private Claims createMockClaims() {
        Claims claims = mock(Claims.class, org.mockito.Answers.RETURNS_DEEP_STUBS);
        lenient().when(claims.getSubject()).thenReturn(username);
        lenient().when(claims.get("userId", String.class)).thenReturn("1L");
        lenient().when(claims.get("role", String.class)).thenReturn(RoleEnum.USER.name());
        lenient().when(claims.getExpiration()).thenReturn(new Date(System.currentTimeMillis() + 3600000));
        return claims;
    }
}

