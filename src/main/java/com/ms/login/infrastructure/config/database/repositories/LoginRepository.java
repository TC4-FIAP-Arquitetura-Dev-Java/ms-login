package com.ms.login.infrastructure.config.database.repositories;

import com.ms.login.infrastructure.config.database.entities.LoginDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LoginRepository extends MongoRepository<LoginDocument, String> {

    LoginDocument findByUsername(String username);
}
