package com.ms.login.domain.model;


public class AuthTokenDomain {

    private String accessToken;
    private String refreshToken;
    private String username;
    private String role;
    private String expiresAt;
    private String userId;

    public AuthTokenDomain(String accessToken, String username, String role, String userId, String expiresAt) {
        this.accessToken = accessToken;
        this.username = username;
        this.role = role;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    // Construtor para login + refresh token (quando houver)
    public AuthTokenDomain(String accessToken, String refreshToken, String username, String role, String userId, String expiresAt) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.username = username;
        this.role = role;
        this.userId = userId;
        this.expiresAt = expiresAt;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(String expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
