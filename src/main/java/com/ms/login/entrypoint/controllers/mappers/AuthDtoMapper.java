package com.ms.login.entrypoint.controllers.mappers;

import com.ms.login.domain.model.AuthTokenDomain;
import com.ms.loginDomain.gen.model.LoginResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface AuthDtoMapper {

    AuthDtoMapper INSTANCE = Mappers.getMapper(AuthDtoMapper.class);

    @Mapping(target = "userId", ignore = true)
    LoginResponseDto toLoginResponse(AuthTokenDomain authTokenDomain);
}
