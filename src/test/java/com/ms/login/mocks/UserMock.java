package com.ms.login.mocks;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.RoleEnumDto;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.dto.UserResponse;

import java.time.OffsetDateTime;

public class UserMock {
    public static UserDomain getUserDomain(){
        return new UserDomain(
                "1",
                "User Test",
                "usernameTest",
                "passwordTest",
                "user@test.com",
                true,
                RoleEnum.ADMIN
        );
    }

    public static UserResponse getUserResponse(){
        return new UserResponse(
                "1",
                "usernameTest",
                "User Test",
                "passwordTest",
                "user@test.com",
                RoleEnumDto.ADMIN,
                true,
                OffsetDateTime.now(),
                OffsetDateTime.now()
        );
    }

    public static UserRequest getUserRequest(){
        return new UserRequest(
                "usernameTest",
                "User Test",
                "passwordTest",
                "user@test.com",
                RoleEnumDto.ADMIN,
                true
        );
    }
}
