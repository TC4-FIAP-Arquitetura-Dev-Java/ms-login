package com.ms.login.domain.model;

public class AuthTokenDomain {

    private String token;
    private String username;
    private String expiresAt;
    private String userId;

    public AuthTokenDomain(String token, String username, String expiresAt, String userId) {
        this.token = token;
        this.username = username;
        this.expiresAt = expiresAt;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
