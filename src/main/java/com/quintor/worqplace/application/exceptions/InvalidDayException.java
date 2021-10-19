package com.quintor.worqplace.application.exceptions;

public class InvalidDayException extends RuntimeException {
    public InvalidDayException() {
        super("The reservation day could not be before today!");
    }
}