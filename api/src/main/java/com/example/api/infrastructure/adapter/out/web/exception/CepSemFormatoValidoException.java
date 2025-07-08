package com.example.api.infrastructure.adapter.out.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CepSemFormatoValidoException extends RuntimeException {
    public CepSemFormatoValidoException(String message) {
        super(message);
    }
}