package com.tui.proof.application.exception.handler;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@RequiredArgsConstructor
class ErrorResponse {

    private final String message;

    private final List<ValidationError> validationErrors;

    public ErrorResponse(String message) {
        this(message, new ArrayList<>());
    }

    @RequiredArgsConstructor
    @Getter
    static class ValidationError {
        private final String field;
        private final String message;
    }
}
