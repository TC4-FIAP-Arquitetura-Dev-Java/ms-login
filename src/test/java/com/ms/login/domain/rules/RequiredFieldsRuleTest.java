package com.ms.login.domain.rules;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.LoginDomain;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RequiredFieldsRuleTest {

    @Test
    void shouldNotThrowExceptionWhenAllFieldsArePresent() {
        // given
        LoginDomain loginDomain = new LoginDomain();
        loginDomain.setUserId("1L");
        loginDomain.setUsername("testuser");
        loginDomain.setPassword("password123");
        loginDomain.setRoleEnum(RoleEnum.USER);

        // when & then
        assertDoesNotThrow(() -> RequiredFieldsRule.checkRequiredFields(loginDomain));
    }

    @Test
    void shouldNotThrowExceptionWhenSomeFieldsAreNull() {
        // given
        LoginDomain loginDomain = new LoginDomain();
        loginDomain.setUserId(null);
        loginDomain.setUsername(null);
        loginDomain.setPassword(null);
        loginDomain.setRoleEnum(RoleEnum.USER);

        // when & then
        assertDoesNotThrow(() -> RequiredFieldsRule.checkRequiredFields(loginDomain));
    }

    @Test
    void shouldNotThrowExceptionWhenSomeFieldsAreEmpty() {
        // given
        LoginDomain loginDomain = new LoginDomain();
        loginDomain.setUserId("");
        loginDomain.setUsername("");
        loginDomain.setPassword("");
        loginDomain.setRoleEnum(RoleEnum.ADMIN);

        // when & then
        assertDoesNotThrow(() -> RequiredFieldsRule.checkRequiredFields(loginDomain));
    }

    @Test
    void shouldNotThrowExceptionWhenFieldsHaveWhitespace() {
        // given
        LoginDomain loginDomain = new LoginDomain();
        loginDomain.setUserId("   ");
        loginDomain.setUsername("   ");
        loginDomain.setPassword("   ");
        loginDomain.setRoleEnum(RoleEnum.USER);

        // when & then
        assertDoesNotThrow(() -> RequiredFieldsRule.checkRequiredFields(loginDomain));
    }
}

