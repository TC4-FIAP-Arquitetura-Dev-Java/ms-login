package com.ms.login.domain.rules;

import com.ms.login.domain.model.LoginDomain;

public class RequiredFieldsRule {

    public static void checkRequiredFields(LoginDomain loginDomain){
        if(isNullOrEmpty(loginDomain.getUserId()) &&
                isNullOrEmpty(loginDomain.getUsername()) &&
                isNullOrEmpty(loginDomain.getPassword()) &&
                isNullOrEmpty(loginDomain.getRoleEnum().toString())){}
    }

    private static boolean isNullOrEmpty(String field) {
        return field == null || field.trim().isEmpty();
    }
}
