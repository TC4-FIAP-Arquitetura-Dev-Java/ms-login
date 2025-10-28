package com.ms.login.entrypoint.controllers.mappers;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.LoginDomain;
import com.ms.loginDomain.gen.model.LoginResponseDto;
import com.ms.loginDomain.gen.model.RegisterRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthDtoMapper {

    AuthDtoMapper INSTANCE = Mappers.getMapper(AuthDtoMapper.class);

    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(source = "roleEnum", target = "roleEnum", qualifiedByName = "toRoleEnum")
    LoginDomain toLoginDomain(RegisterRequestDto registerRequestDto);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "roleEnum", ignore = true)
    LoginResponseDto toLoginResponseDto(AuthTokenDomain authTokenDomain);

    @Named("toRoleEnum")
    default RoleEnum mapRole(String role) {
        if (role == null) return null;
        return RoleEnum.valueOf(role.toUpperCase());
    }
}