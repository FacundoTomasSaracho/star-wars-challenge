package org.facundosaracho.starwarschallenge.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {


    SWAPI_CLIENT_ERROR(5000, "Unexpected error happened while trying to invoke star wars api."),
    SWAPI_PEOPLE_NOT_FOUND(4000, "People was not found with the provided parameter"),


    INTERNAL_SERVER_ERROR(5000, "Unexpected error"),
    MANDATORY_PARAMETER_IS_MISSING(4001,"At least one mandatory parameter is missing."),

    PASSWORD_MISSING(4002, "Password must not be null."),
    INVALID_CREDENTIALS(4003, "Invalid credentials.");

    private final int code;
    private final String message;


    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
