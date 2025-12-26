package com.ms.login.domain.model;

import com.ms.login.domain.enums.RoleEnum;

public class UserDomain {

    private String userId;
    private String name;
    private String username;
    private String password;
    private String email;
    private Boolean activeUser;
    private RoleEnum roleEnum;

    public UserDomain(String userId, String name, String username, String password,
                      String email, Boolean activeUser, RoleEnum roleEnum) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.activeUser = activeUser;
        this.roleEnum = roleEnum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getActiveUser() {
        return activeUser;
    }

    public void setActiveUser(Boolean activeUser) {
        this.activeUser = activeUser;
    }

    public RoleEnum getRoleEnum() {
        return roleEnum;
    }

    public void setRoleEnum(RoleEnum roleEnum) {
        this.roleEnum = roleEnum;
    }
}
