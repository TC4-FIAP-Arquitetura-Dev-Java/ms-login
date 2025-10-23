package com.ms.login.domain.enums;

public enum RoleEnum {
    ADMIN,
    USER;

    public String toAuthority() {
        return "ROLE_" + this.name();
    }

    public static RoleEnum fromAuthority(String authority) {
        String name = authority.startsWith("ROLE_") ? authority.substring(5) : authority;
        return RoleEnum.valueOf(name);
    }
}
