package com.quintor.worqplace.application.exceptions;

public class WorkplaceNotFoundException extends RuntimeException {
    public WorkplaceNotFoundException(String message) {
        super(message);
    }
}