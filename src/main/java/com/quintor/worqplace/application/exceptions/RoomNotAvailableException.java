package com.quintor.worqplace.application.exceptions;

public class RoomNotAvailableException extends RuntimeException {
    public RoomNotAvailableException() {
        super("Room is not available");
    }
}
