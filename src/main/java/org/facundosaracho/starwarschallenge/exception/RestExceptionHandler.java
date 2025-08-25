package org.facundosaracho.starwarschallenge.exception;

import org.facundosaracho.starwarschallenge.exception.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.util.stream.Collectors;

import static org.facundosaracho.starwarschallenge.exception.dto.ErrorCodeDto.INTERNAL_SERVER_ERROR;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDto> handleException() {
        return new ResponseEntity<>(new ErrorDto(INTERNAL_SERVER_ERROR.getCode(),
                INTERNAL_SERVER_ERROR.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorDto> handleException(BusinessException businessException) {
        return new ResponseEntity<>(formErrorDto(businessException), businessException.getHttpStatus());
    }

    @ExceptionHandler(ClientException.class)
    public ResponseEntity<ErrorDto> handleException(ClientException clientException) {
        return new ResponseEntity<>(formErrorDto(clientException), clientException.getHttpStatus());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDto> handleException(MissingServletRequestParameterException e) {
        return new ResponseEntity<>(
                formErrorDto(HttpStatus.BAD_REQUEST.value(),
                        String.format("Missing mandatory paramter: '%s", e.getParameterName())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handleValidationExceptions(MethodArgumentNotValidException ex) {
        String errorMessages = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .collect(Collectors.joining(", "));

        ErrorDto errorDto = new ErrorDto(
                HttpStatus.BAD_REQUEST.value(),
                errorMessages
        );

        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErrorDto> handleException(HandlerMethodValidationException e) {
        String message = "Missing mandatory parameters. Check documentation.";

        return new ResponseEntity<>(
                formErrorDto(HttpStatus.BAD_REQUEST.value(), message),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleException (BadCredentialsException e) {

        return new ResponseEntity<>(formErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CredentialsExpiredException.class)
    public ResponseEntity<ErrorDto> handleException (CredentialsExpiredException e) {

        return new ResponseEntity<>(formErrorDto(HttpStatus.UNAUTHORIZED.value(), e.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    private <T extends RestException> ErrorDto formErrorDto(T exception) {
        return new ErrorDto(exception.getCode(), exception.getMessage());
    }

    private ErrorDto formErrorDto(Integer code, String message) {
        return new ErrorDto(code, message);
    }

}
