package com.ms.login.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CredentialLoginNotFoundException extends ResponseStatusException {
    public CredentialLoginNotFoundException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
