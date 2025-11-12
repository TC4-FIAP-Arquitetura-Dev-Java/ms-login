package com.ms.login.domain.model;

import com.ms.login.domain.enums.RoleEnum;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDomainTest {

    @Test
    void constructorWithBasicFields_shouldSetFields() {
        String accessToken = UUID.randomUUID().toString();
        String username = "password";
        String roleEnum = RoleEnum.USER.name();
        String expiresAt =  UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        AuthTokenDomain authTokenDomain = new AuthTokenDomain(accessToken, username, roleEnum, userId, expiresAt);

        assertEquals(accessToken, authTokenDomain.getAccessToken());
        assertEquals(username, authTokenDomain.getUsername());
        assertEquals(roleEnum, authTokenDomain.getRoleEnum());
        assertEquals(expiresAt, authTokenDomain.getExpiresAt());
        assertEquals(userId, authTokenDomain.getUserId());
    }

    @Test
    void constructorWithAllFields_shouldSetAllFields() {
        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();
        String username = "password";
        String roleEnum = RoleEnum.USER.name();
        String expiresAt =  UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        AuthTokenDomain authTokenDomain = new AuthTokenDomain(accessToken, refreshToken, username, roleEnum, userId, expiresAt);

        assertEquals(accessToken, authTokenDomain.getAccessToken());
        assertEquals(refreshToken, authTokenDomain.getRefreshToken());
        assertEquals(username, authTokenDomain.getUsername());
        assertEquals(roleEnum, authTokenDomain.getRoleEnum());
        assertEquals(expiresAt, authTokenDomain.getExpiresAt());
        assertEquals(userId, authTokenDomain.getUserId());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        String accessToken = UUID.randomUUID().toString();
        String refreshToken = UUID.randomUUID().toString();
        String username = "password";
        String roleEnum = RoleEnum.USER.name();
        String expiresAt =  UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        AuthTokenDomain authTokenDomain = new AuthTokenDomain(accessToken, refreshToken, username, roleEnum, userId, expiresAt);

        assertEquals(accessToken, authTokenDomain.getAccessToken());
        assertEquals(refreshToken, authTokenDomain.getRefreshToken());
        assertEquals(username, authTokenDomain.getUsername());
        assertEquals(roleEnum, authTokenDomain.getRoleEnum());
        assertEquals(expiresAt, authTokenDomain.getExpiresAt());
        assertEquals(userId, authTokenDomain.getUserId());
    }
}