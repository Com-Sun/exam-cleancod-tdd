package com.nhnacademy.exam.exceptions;

public class TruckCanNotParkException extends RuntimeException{
    public TruckCanNotParkException(String message) {
        super(message);
    }
}
