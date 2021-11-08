package com.tui.proof.common.exception;

import lombok.Getter;

@Getter
public class OrderModificationDeniedException extends RuntimeException{
    private final String processingExceptionMessage;

    public OrderModificationDeniedException(String processingExceptionMessage) {
        this.processingExceptionMessage = processingExceptionMessage;
    }
}
