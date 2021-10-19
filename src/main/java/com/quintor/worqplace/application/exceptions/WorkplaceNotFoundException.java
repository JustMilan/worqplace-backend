package com.quintor.worqplace.application.exceptions;

public class WorkplaceNotFoundException extends RuntimeException {
    public WorkplaceNotFoundException(Long id) {
        super("Workplace " + id + " not found");
    }
}
