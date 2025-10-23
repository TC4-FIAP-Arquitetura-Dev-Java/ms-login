package com.ms.login.domain.model;

@Deprecated
public class CredentialDomain {

    private String username;
    private String password;

    public CredentialDomain(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public CredentialDomain() {
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
