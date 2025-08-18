package org.facundosaracho.starwarschallenge.exception;

import org.springframework.http.HttpStatus;

public class ClientException extends RestException {
    public ClientException(String message, int code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }
}
