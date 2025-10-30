package com.ms.login.domain.model;

import java.time.LocalDateTime;

public class TokenInfoDomain {

    private final String userId;
    private final String username;
    private final LocalDateTime expiresAt;
    private boolean revoked;

    public TokenInfoDomain(String userId, String username, LocalDateTime expiresAt) {
        this.userId = userId;
        this.username = username;
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
