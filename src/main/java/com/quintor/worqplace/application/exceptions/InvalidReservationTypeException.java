package com.quintor.worqplace.application.exceptions;

public class InvalidReservationTypeException extends RuntimeException {
    public InvalidReservationTypeException() {
        super("A choice must be made to reserve for a room or a workplace!");
    }
}