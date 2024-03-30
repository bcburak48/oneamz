package com.oneamz.inventory.exception;

import org.slf4j.LoggerFactory;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
        LoggerFactory.getLogger(ResourceNotFoundException.class).error(message);
    }
}