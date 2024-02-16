package com.dholubeu.rideservice.domain.exception;

public class ResourceDoesNotExistException extends RuntimeException {

    public ResourceDoesNotExistException(String message) {
        super(message);
    }

}