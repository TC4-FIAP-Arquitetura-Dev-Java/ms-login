package com.ms.login.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CredentialLoginAlreadyExistsException extends ResponseStatusException {
    public CredentialLoginAlreadyExistsException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
