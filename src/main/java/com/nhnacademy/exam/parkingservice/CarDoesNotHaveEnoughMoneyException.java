package com.nhnacademy.exam.parkingservice;

public class CarDoesNotHaveEnoughMoneyException extends RuntimeException{
    public CarDoesNotHaveEnoughMoneyException(String message) {
        super(message);
    }
}
