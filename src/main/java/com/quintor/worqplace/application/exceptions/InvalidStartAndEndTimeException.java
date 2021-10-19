package com.quintor.worqplace.application.exceptions;

public class InvalidStartAndEndTimeException extends RuntimeException {
    public InvalidStartAndEndTimeException() {
        super("The reservation start time cannot be after the end time!");
    }
}
