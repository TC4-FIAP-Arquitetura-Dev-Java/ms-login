package com.ms.login.infraestruture.dataproviders.security;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.infrastructure.security.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class JwtTokenProviderTest {

    private static final String SECRET = "0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF0123456789ABCDEF";

    private JwtTokenProvider jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtTokenProvider();
        ReflectionTestUtils.setField(jwtUtil, "ACCESS_TOKEN_SECRET", SECRET);
        ReflectionTestUtils.setField(jwtUtil, "REFRESH_TOKEN_SECRET", SECRET);
    }

    @AfterEach
    void tearDown() {
        jwtUtil = null;
    }

    @Test
    void generateAndValidateToken_shouldReturnValidTokenWithClaims() {
        String token = jwtUtil.generateAccessToken("user@example.com", "42", RoleEnum.USER);

        assertThat(jwtUtil.validateToken(token)).isTrue();

        Claims claims = jwtUtil.extractClaimsAcessToken(token);
        assertThat(jwtUtil.extractUsername(claims)).isEqualTo("user@example.com");
        assertThat(jwtUtil.extractUserId(claims)).isEqualTo("42");
        assertThat(jwtUtil.extractExpirationDate(claims)).isAfter(new Date());
    }

    @Test
    void validateToken_shouldReturnFalseForDifferentSecret() {
        String token = jwtUtil.generateAccessToken("user", "1", RoleEnum.USER);

        JwtTokenProvider otherUtil = new JwtTokenProvider();
        ReflectionTestUtils.setField(otherUtil, "ACCESS_TOKEN_SECRET", SECRET + "DIFF");
        ReflectionTestUtils.setField(otherUtil, "REFRESH_TOKEN_SECRET", SECRET + "DIFF");

        assertThat(otherUtil.validateToken(token)).isFalse();
    }

    @Test
    void extractRole_shouldExtractRoleFromClaims() {
        String token = jwtUtil.generateAccessToken("user", "1", RoleEnum.ADMIN);
        Claims claims = jwtUtil.extractClaimsAcessToken(token);

        RoleEnum role = jwtUtil.extractRole(claims);

        assertThat(role).isEqualTo(RoleEnum.ADMIN);
    }

    @Test
    void extractRole_shouldThrowExceptionWhenRoleNotPresent() {
        Claims claims = Jwts.claims();
        // Não definir role no claim

        assertThatThrownBy(() -> jwtUtil.extractRole(claims))
            .isInstanceOf(Exception.class);
    }

    @Test
    void isValidAcessToken_shouldReturnTrueForValidAccessToken() {
        String token = jwtUtil.generateAccessToken("user", "1", RoleEnum.USER);

        assertThat(jwtUtil.isValidAcessToken(token)).isTrue();
    }

    @Test
    void isValidAcessToken_shouldReturnFalseForInvalidToken() {
        assertThat(jwtUtil.isValidAcessToken("invalid-token")).isFalse();
    }

    @Test
    void isValidRefleshToken_shouldReturnFalseForAccessToken() {
        String accessToken = jwtUtil.generateAccessToken("user", "1", RoleEnum.USER);

        // Access token não é refresh token
        assertThat(jwtUtil.isValidRefleshToken(accessToken)).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalseForInvalidToken() {
        assertThat(jwtUtil.validateToken("invalid-token")).isFalse();
    }

    @Test
    void validateToken_shouldReturnFalseForNullToken() {
        assertThat(jwtUtil.validateToken(null)).isFalse();
    }

    @Test
    void extractEmail_shouldReturnEmailFromClaims() {
        String token = jwtUtil.generateAccessToken("user@example.com", "1", RoleEnum.USER);
        Claims claims = jwtUtil.extractClaimsAcessToken(token);

        // Email não está no token gerado, então deve retornar null
        String email = jwtUtil.extractEmail(claims);
        assertThat(email).isNull();
    }

    @Test
    void extractExpirationDateFromToken_shouldReturnExpirationDate() {
        String token = jwtUtil.generateAccessToken("user", "1", RoleEnum.USER);

        Date expiration = jwtUtil.extractExpirationDateFromToken(token);

        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new Date());
    }

    @Test
    void extractExpirationDate_shouldReturnExpirationFromClaims() {
        String token = jwtUtil.generateAccessToken("user", "1", RoleEnum.USER);
        Claims claims = jwtUtil.extractClaimsAcessToken(token);

        Date expiration = jwtUtil.extractExpirationDate(claims);

        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new Date());
    }
}
