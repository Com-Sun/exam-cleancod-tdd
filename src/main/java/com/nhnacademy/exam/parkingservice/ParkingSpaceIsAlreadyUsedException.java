package com.nhnacademy.exam.parkingservice;

public class ParkingSpaceIsAlreadyUsedException extends RuntimeException{
    public ParkingSpaceIsAlreadyUsedException(String message) {
        super(message);
    }
}
