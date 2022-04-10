package com.nhnacademy.exam.exceptions;

public class CarDoesNotHaveEnoughMoneyException extends RuntimeException{
    public CarDoesNotHaveEnoughMoneyException(String message) {
        super(message);
    }
}
