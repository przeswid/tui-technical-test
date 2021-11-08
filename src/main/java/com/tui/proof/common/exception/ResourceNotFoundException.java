package com.tui.proof.common.exception;

import lombok.Getter;

@Getter
public class ResourceNotFoundException extends RuntimeException {
    private final Object resourceIdentifier;

    public ResourceNotFoundException(Object resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }
}
