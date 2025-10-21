package com.ms.login.domain.model;

import java.time.Instant;

public class AuthTokenDomain {

    private String accessToken;
    private String refreshToken;
    private Instant expiresAt;

    public AuthTokenDomain(String accessToken, String refreshToken, Instant expiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresAt = expiresAt;
    }

    public AuthTokenDomain() {}

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Instant getExpiresAt() {
        return expiresAt;
    }
}
