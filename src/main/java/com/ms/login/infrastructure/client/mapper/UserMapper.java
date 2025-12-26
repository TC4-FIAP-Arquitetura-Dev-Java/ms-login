package com.ms.login.infrastructure.client.mapper;

import com.ms.login.domain.enums.RoleEnum;
import com.ms.login.domain.model.UserDomain;
import com.ms.login.infrastructure.client.dto.UserRequest;
import com.ms.login.infrastructure.client.dto.UserResponse;
import com.ms.login.infrastructure.client.dto.RoleEnumDto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    // RESPONSE → DOMAIN (Feign → interno)
    @Mapping(source = "id", target = "userId")
    @Mapping(source = "roleEnum", target = "roleEnum", qualifiedByName = "dtoToDomain")
    UserDomain toUserDomain(UserResponse userResponse);

    // DOMAIN → REQUEST (interno → Feign)
    @Mapping(source = "roleEnum", target = "roleEnumDto", qualifiedByName = "domainToDto")
    UserRequest toUserRequest(UserDomain userDomain);

    // ================= MAPEAMENTO DO ENUM =================

    @Named("dtoToDomain")
    default RoleEnum dtoToDomain(RoleEnumDto dto){
        return dto == null ? null : RoleEnum.valueOf(dto.name());
    }

    @Named("domainToDto")
    default RoleEnumDto domainToDto(RoleEnum domain){
        return domain == null ? null : RoleEnumDto.valueOf(domain.name());
    }
}