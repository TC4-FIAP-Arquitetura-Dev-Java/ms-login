package com.ms.login.infrastructure.client.dto;

public record UserRequest(
        String username,
        String name,
        String password,
        String email,
        Boolean activeUser) {
}
