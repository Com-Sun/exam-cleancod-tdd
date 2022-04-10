package com.nhnacademy.exam.exceptions;

public class UserDoesNotHaveEnoughMoneyException extends RuntimeException{
    public UserDoesNotHaveEnoughMoneyException(String message) {
        super(message);
    }
}
