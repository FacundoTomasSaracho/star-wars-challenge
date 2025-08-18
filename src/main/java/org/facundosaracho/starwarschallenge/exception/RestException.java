package org.facundosaracho.starwarschallenge.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class RestException extends RuntimeException {
    private final String message;
    private final int code;
    private final HttpStatus httpStatus;


    RestException(String message, int code, HttpStatus httpStatus) {
        this.message = message;
        this.code = code;
        this.httpStatus = httpStatus;
    }
}
