package com.tui.proof.common.exception;

import lombok.Getter;

@Getter
public class OrderProcessingException extends RuntimeException{
    private final String processingExceptionMessage;

    public OrderProcessingException(String processingExceptionMessage) {
        this.processingExceptionMessage = processingExceptionMessage;
    }
}
