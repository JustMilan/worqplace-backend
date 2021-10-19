package com.quintor.worqplace.application.exceptions;

public class EmployeeNotFoundException extends RuntimeException {
    public EmployeeNotFoundException(Long id) {
        super("Employee " + id + " not found");
    }
}
