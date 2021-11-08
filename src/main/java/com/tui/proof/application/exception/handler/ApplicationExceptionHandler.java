package com.tui.proof.application.exception.handler;

import com.tui.proof.common.exception.ResourceNotFoundException;
import com.tui.proof.common.i18n.Messages;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(MethodArgumentNotValidException exc) {
        log.error("Validation error occurred", exc);
        ErrorResponse errorMessage = new ErrorResponse(Messages.VALIDATION_FAILED_MSG,
                exc.getBindingResult().getFieldErrors().stream()
                        .map(f -> new ErrorResponse.ValidationError(f.getField(), f.getDefaultMessage()))
                        .collect(Collectors.toList()));

        log.error("Sending error response: {}", errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ResourceNotFoundException exc) {
        log.error("Resource not found error occurred", exc);
        ErrorResponse errorMessage = new ErrorResponse(Messages.RESOURCE_NOT_FOUNG_MSG + "{" + exc.getResourceIdentifier() + "}");

        log.error("Sending error response: {}", errorMessage);
        return ResponseEntity.badRequest().body(errorMessage);
    }
}
