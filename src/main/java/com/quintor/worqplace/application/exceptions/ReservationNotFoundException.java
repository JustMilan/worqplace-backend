package com.quintor.worqplace.application.exceptions;

public class ReservationNotFoundException extends RuntimeException {
    public ReservationNotFoundException(Long id) {
        super("Reservation " + id + " not found");
    }
}
