package com.quintor.worqplace.application.exceptions;

public class LocationNotFoundException extends RuntimeException {
    public LocationNotFoundException(Long id) {
        super("Location " + id + " not found");
    }
}
