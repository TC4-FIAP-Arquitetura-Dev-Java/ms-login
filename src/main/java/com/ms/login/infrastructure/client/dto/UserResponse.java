package com.ms.login.infrastructure.client.dto;

import java.time.OffsetDateTime;

public record UserResponse(
        String id,
        String username,
        String name,
        String password,
        String email,
        RoleEnumDto roleEnum,
        Boolean activeUser,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}
