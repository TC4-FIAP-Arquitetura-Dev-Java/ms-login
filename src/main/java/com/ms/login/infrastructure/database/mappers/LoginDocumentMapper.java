package com.ms.login.infrastructure.database.mappers;

import com.ms.login.domain.model.LoginDomain;
import com.ms.login.infrastructure.database.entities.LoginDocument;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.ERROR)
public interface LoginDocumentMapper {
    
    LoginDocumentMapper INSTANCE = Mappers.getMapper(LoginDocumentMapper.class);

    @Mapping(target = "createdAt", expression = "java(loginDocument.getCreatedAt() != null ? loginDocument.getCreatedAt().atOffset(java.time.ZoneOffset.UTC) : null)")
    @Mapping(target = "updatedAt", expression = "java(loginDocument.getUpdatedAt() != null ? loginDocument.getUpdatedAt().atOffset(java.time.ZoneOffset.UTC) : null)")
    LoginDomain toDomain(LoginDocument loginDocument);

    @Mapping(target = "createdAt", expression = "java(loginDomain.getCreatedAt() != null ? loginDomain.getCreatedAt().toInstant() : null)")
    @Mapping(target = "updatedAt", expression = "java(loginDomain.getUpdatedAt() != null ? loginDomain.getUpdatedAt().toInstant() : null)")
    LoginDocument toDocument(LoginDomain loginDomain);
    
}
