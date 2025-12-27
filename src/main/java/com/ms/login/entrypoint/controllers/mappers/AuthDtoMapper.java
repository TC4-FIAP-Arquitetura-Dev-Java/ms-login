package com.ms.login.entrypoint.controllers.mappers;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.login.domain.model.LoginDomain;
import com.ms.loginDomain.gen.model.LoginResponseDto;
import com.ms.loginDomain.gen.model.RefreshResponseDto;
import com.ms.loginDomain.gen.model.RegisterRequestDto;
import com.ms.loginDomain.gen.model.RoleEnumDto;
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
    @Mapping(target = "userId", ignore = true)
    LoginDomain toLoginDomain(RegisterRequestDto registerRequestDto);

    @Mapping(source = "roleEnum", target = "roleEnum", qualifiedByName = "toRoleEnumDto")
    LoginResponseDto toLoginResponseDto(AuthTokenDomain authTokenDomain);

    RefreshResponseDto toRefreshResponseDto(AuthTokenDomain authTokenDomain);

    @Named("toRoleEnum")
    default RoleEnum mapRole(String role) {
        if (role == null) return null;
        return RoleEnum.valueOf(role.toUpperCase());
    }

    @Named("toRoleEnumDto")
    default RoleEnumDto mapRoleDto(String role) {
        if (role == null) return null;
        return RoleEnumDto.valueOf(role.toUpperCase());
    }
}