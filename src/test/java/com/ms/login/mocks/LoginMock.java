package com.ms.login.mocks;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.LoginDomain;
import com.ms.login.infrastructure.database.entities.LoginDocument;
import com.ms.loginDomain.gen.model.LoginRequestDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

public class LoginMock {

    public static LoginDomain getLoginDomain(){
        LoginDomain domain = new LoginDomain();
        domain.setUserId("1L");
        domain.setUsername("username");
        domain.setPassword("password");
        domain.setRoleEnum(RoleEnum.ADMIN);
        domain.setCreatedAt(OffsetDateTime.now());
        domain.setUpdatedAt(OffsetDateTime.now());
        return domain;
    }

    public static LoginRequestDto getLoginRequestDto(){
        LoginRequestDto loginRequestDto = new LoginRequestDto(
                "username",
                "password"
        );
        return loginRequestDto;
    }

    public static LoginDocument getLoginDocument(){
        LoginDocument loginDocument = new LoginDocument();
        loginDocument.setUserId("1L");
        loginDocument.setUsername("username");
        loginDocument.setPassword("password");
        loginDocument.setRoleEnum(RoleEnum.ADMIN);
        loginDocument.setCreatedAt(Instant.now());
        loginDocument.setUpdatedAt(Instant.now());
        return loginDocument;
    }

    public static List<LoginDocument> getLoginDocuments(){return List.of(getLoginDocument());}

    public static List<LoginDomain> getLoginDomains(){return List.of(getLoginDomain());}
}
