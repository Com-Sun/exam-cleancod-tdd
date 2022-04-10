package com.nhnacademy.exam.exceptions;

public class ParkingSpaceIsAlreadyUsedException extends RuntimeException{
    public ParkingSpaceIsAlreadyUsedException(String message) {
        super(message);
    }
}
