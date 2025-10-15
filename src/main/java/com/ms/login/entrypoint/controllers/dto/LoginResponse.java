package com.ms.login.entrypoint.controllers.dto;

public record LoginResponse (
        String token,
        String username,
        String expiresAt,
        String userId){
}
