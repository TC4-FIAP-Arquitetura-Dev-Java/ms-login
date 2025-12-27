package com.ms.login.domain.rules;

import com.ms.login.domain.exceptions.FieldRequiredException;
import com.ms.login.infrastructure.client.dto.UserRequest;

public class RequiredFieldsRule {

    public static void checkRequiredFields(UserRequest request) {
        if (isNullOrEmpty(request.username()) ||
                isNullOrEmpty(request.name()) ||
                isNullOrEmpty(request.password()) ||
                isNullOrEmpty(request.email()) ||
                request.roleEnum() == null) {
            throw new FieldRequiredException("There are some required fields empty");
        }
    }

    private static boolean isNullOrEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }
}
