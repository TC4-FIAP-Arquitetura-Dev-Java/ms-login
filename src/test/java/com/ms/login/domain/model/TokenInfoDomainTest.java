package com.ms.login.domain.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class TokenInfoDomainTest {

    @Test
    void constructorWithAllFields_shouldSetAllFields() {
        String userId = UUID.randomUUID().toString();
        String username = "username";
        LocalDateTime expiresAt = LocalDateTime.now();
        Boolean isRevoked = false;

        TokenInfoDomain tokenInfoDomain = new TokenInfoDomain(userId, username, expiresAt);

        assertEquals(userId, tokenInfoDomain.getUserId());
        assertEquals(username, tokenInfoDomain.getUsername());
        assertEquals(expiresAt, tokenInfoDomain.getExpiresAt());
        assertEquals(isRevoked, tokenInfoDomain.isRevoked());
    }
}