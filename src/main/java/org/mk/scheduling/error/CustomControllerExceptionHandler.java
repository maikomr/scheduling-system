package org.mk.scheduling.error;

import org.mk.scheduling.exceptions.UniqueConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.validation.ConstraintViolationException;

/**
 * Created by maiko on 23/12/2016.
 */
@RestControllerAdvice
public class CustomControllerExceptionHandler {

    @ExceptionHandler(value = { UniqueConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CustomError uniqueConstraintViolationException(UniqueConstraintViolationException ex) {
        return new CustomError(ex.getProperty(), ex.getMessage());
    }

    @ExceptionHandler(value = { ConstraintViolationException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrorResponse constraintViolationException(ConstraintViolationException ex) {
        return new ApiErrorResponse(500, 5001, ex.getMessage());
    }

    @ExceptionHandler(value = { NoHandlerFoundException.class })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiErrorResponse noHandlerFoundException(Exception ex) {
        return new ApiErrorResponse(404, 4041, ex.getMessage());
    }
}
