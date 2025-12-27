package com.ms.login.domain.rules;

import com.ms.login.domain.exceptions.FieldRequiredException;
import com.ms.login.infrastructure.client.dto.RoleEnumDto;
import com.ms.login.infrastructure.client.dto.UserRequest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequiredFieldsRuleTest {

    // -------------------- TODOS OS CAMPOS PRESENTES --------------------
    @Test
    void shouldNotThrowExceptionWhenAllFieldsArePresent() {
        UserRequest request = new UserRequest(
                "testuser",      // username
                "John Doe",      // name
                "password123",   // password
                "user@test.com", // email
                RoleEnumDto.USER,// roleEnum
                true             // activeUser
        );

        assertDoesNotThrow(() -> RequiredFieldsRule.checkRequiredFields(request));
    }

    // -------------------- CAMPOS NULOS --------------------
    @Test
    void shouldThrowExceptionWhenSomeFieldsAreNull() {
        UserRequest request = new UserRequest(
                null,            // username
                null,            // name
                null,            // password
                null,            // email
                RoleEnumDto.USER,// roleEnum
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }

    // -------------------- CAMPOS VAZIOS --------------------
    @Test
    void shouldThrowExceptionWhenSomeFieldsAreEmpty() {
        UserRequest request = new UserRequest(
                "",              // username
                "",              // name
                "",              // password
                "",              // email
                RoleEnumDto.ADMIN,// roleEnum
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }

    // -------------------- CAMPOS COM ESPAÇOS --------------------
    @Test
    void shouldThrowExceptionWhenFieldsHaveWhitespace() {
        UserRequest request = new UserRequest(
                "   ",           // username
                "   ",           // name
                "   ",           // password
                "   ",           // email
                RoleEnumDto.USER,// roleEnum
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }

    // -------------------- TESTES DE CAMPOS INDIVIDUAIS --------------------
    @Test
    void shouldThrowExceptionWhenOnlyUsernameIsNull() {
        UserRequest request = new UserRequest(
                null,             // username
                "John Doe",       // name
                "password123",    // password
                "user@test.com",  // email
                RoleEnumDto.USER, // roleEnum
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }

    @Test
    void shouldThrowExceptionWhenOnlyNameIsNull() {
        UserRequest request = new UserRequest(
                "testuser",       // username
                null,             // name
                "password123",    // password
                "user@test.com",  // email
                RoleEnumDto.USER, // roleEnum
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }

    @Test
    void shouldThrowExceptionWhenOnlyPasswordIsNull() {
        UserRequest request = new UserRequest(
                "testuser",
                "John Doe",
                null,
                "user@test.com",
                RoleEnumDto.USER,
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }

    @Test
    void shouldThrowExceptionWhenOnlyEmailIsNull() {
        UserRequest request = new UserRequest(
                "testuser",
                "John Doe",
                "password123",
                null,
                RoleEnumDto.USER,
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }

    @Test
    void shouldThrowExceptionWhenOnlyRoleIsNull() {
        UserRequest request = new UserRequest(
                "testuser",
                "John Doe",
                "password123",
                "user@test.com",
                null,
                true
        );

        assertThrows(FieldRequiredException.class, () -> RequiredFieldsRule.checkRequiredFields(request));
    }
}


