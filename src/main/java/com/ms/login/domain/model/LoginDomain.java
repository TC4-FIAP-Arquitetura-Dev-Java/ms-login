package com.ms.login.domain.model;

import com.ms.login.domain.enums.RoleEnum;
import java.time.OffsetDateTime;

public class LoginDomain {

    private String userId;
    private String username;
    private String password;
    private RoleEnum roleEnum;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public LoginDomain() {
    }

    public LoginDomain(String userId, String username, String password, RoleEnum roleEnum) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roleEnum = roleEnum;
    }

    public LoginDomain(String userId, String username, String password, RoleEnum roleEnum, OffsetDateTime createdAt, OffsetDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.roleEnum = roleEnum;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(OffsetDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
