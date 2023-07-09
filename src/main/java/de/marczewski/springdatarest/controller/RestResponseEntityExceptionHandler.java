package de.marczewski.springdatarest.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value
            = {ConstraintViolationException.class})
    protected ResponseEntity<Object> handleConflict(
            ConstraintViolationException ex, WebRequest request) {

        return handleExceptionInternal(ex, buildMessage(ex),
                new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    /**
     * Build an error message from the constraint violations.
     * contains only one error, because ConstraintViolationException contains only the first error
     * @param ex ConstraintViolationException
     * @return Error message as String
     */
    private String buildMessage(ConstraintViolationException ex) {
        StringBuilder message = new StringBuilder();
        ex.getConstraintViolations().forEach((violation) -> {
            message.append("%s: %s\n".formatted(violation.getPropertyPath(), violation.getMessage()));
        });
        return message.toString();
    }
}