package com.ms.login.domain.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class FieldRequiredException extends ResponseStatusException {

    public FieldRequiredException(String mensagem) {
        super(HttpStatus.BAD_REQUEST, mensagem);
    }
}