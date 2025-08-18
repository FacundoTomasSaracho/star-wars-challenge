package org.facundosaracho.starwarschallenge.exception;

import org.springframework.http.HttpStatus;

public class BusinessException extends RestException {

    public BusinessException(String message, int code, HttpStatus httpStatus) {
        super(message, code, httpStatus);
    }
}
