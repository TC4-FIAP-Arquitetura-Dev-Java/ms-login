package com.ms.login.domain.model;

import com.ms.login.domain.enums.RoleEnum;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class LoginDomainTest {

    @Test
    void defaultConstructor_shouldCreateEmptyUser(){
        LoginDomain loginDomain = new LoginDomain();
        assertNull(loginDomain.getUserId());
        assertNull(loginDomain.getUsername());
        assertNull(loginDomain.getPassword());
        assertNull(loginDomain.getRoleEnum());
        assertNull(loginDomain.getCreatedAt());
        assertNull(loginDomain.getUpdatedAt());
    }

    @Test
    void constructorWithBasicFields_shouldSetFields() {
        String userId = UUID.randomUUID().toString();
        String username = "username";
        String password = "password";
        RoleEnum roleEnum = RoleEnum.USER;

        LoginDomain loginDomain = new LoginDomain(userId, username, password, roleEnum);

        assertEquals(userId, loginDomain.getUserId());
        assertEquals(username, loginDomain.getUsername());
        assertEquals(password, loginDomain.getPassword());
        assertEquals(roleEnum, loginDomain.getRoleEnum());
    }

    @Test
    void constructorWithAllFields_shouldSetAllFields() {
        String userId = UUID.randomUUID().toString();
        String username = "username";
        String password = "password";
        RoleEnum roleEnum = RoleEnum.USER;
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        LoginDomain loginDomain = new LoginDomain(userId, username, password, roleEnum, createdAt, updatedAt);

        assertEquals(userId, loginDomain.getUserId());
        assertEquals(username, loginDomain.getUsername());
        assertEquals(password, loginDomain.getPassword());
        assertEquals(roleEnum, loginDomain.getRoleEnum());
        assertEquals(createdAt, loginDomain.getCreatedAt());
        assertEquals(updatedAt, loginDomain.getUpdatedAt());
    }

    @Test
    void settersAndGetters_shouldWorkCorrectly() {
        String userId = UUID.randomUUID().toString();
        String username = "username";
        String password = "password";
        RoleEnum roleEnum = RoleEnum.USER;
        OffsetDateTime createdAt = OffsetDateTime.now();
        OffsetDateTime updatedAt = OffsetDateTime.now();

        LoginDomain loginDomain = new LoginDomain();
        loginDomain.setUserId(userId);
        loginDomain.setUsername(username);
        loginDomain.setPassword(password);
        loginDomain.setRoleEnum(roleEnum);
        loginDomain.setCreatedAt(createdAt);
        loginDomain.setUpdatedAt(updatedAt);

        assertEquals(userId, loginDomain.getUserId());
        assertEquals(username, loginDomain.getUsername());
        assertEquals(password, loginDomain.getPassword());
        assertEquals(roleEnum, loginDomain.getRoleEnum());
        assertEquals(createdAt, loginDomain.getCreatedAt());
        assertEquals(updatedAt, loginDomain.getUpdatedAt());
    }
}