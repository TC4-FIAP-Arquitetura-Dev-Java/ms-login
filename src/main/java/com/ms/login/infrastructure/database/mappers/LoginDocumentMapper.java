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

    @Mapping(target = "dataCriacao", expression = "java(loginDocument.getDataCriacao() != null ? loginDocument.getDataCriacao().atOffset(java.time.ZoneOffset.UTC) : null)")
    @Mapping(target = "dataAlteracao", expression = "java(loginDocument.getDataAlteracao() != null ? loginDocument.getDataAlteracao().atOffset(java.time.ZoneOffset.UTC) : null)")
    LoginDomain toDomain(LoginDocument loginDocument);

    @Mapping(target = "dataCriacao", expression = "java(loginDomain.getDataCriacao() != null ? loginDomain.getDataCriacao().toInstant() : null)")
    @Mapping(target = "dataAlteracao", expression = "java(loginDomain.getDataAlteracao() != null ? loginDomain.getDataAlteracao().toInstant() : null)")
    LoginDocument toDocument(LoginDomain loginDomain);
    
}
