package com.ms.login.infrastructure.config.database.entities;

import com.ms.login.domain.enums.RoleEnum;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

public class LoginDocument {

    @Id
    private String userId;

    @Indexed(unique = true)
    private String username;

    private String password;

    private RoleEnum roleEnum;

    @CreatedDate
    private Instant createdAt;

    @CreatedDate
    private Instant updatedAt;
}
