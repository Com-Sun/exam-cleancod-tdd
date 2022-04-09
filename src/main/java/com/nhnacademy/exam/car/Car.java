package com.nhnacademy.exam.car;

import java.util.Objects;

public class Car {
    CarType carType;
    int carNumber;

    public Car(CarType carType, int number) {
        this.carType = carType;
        this.carNumber = number;
    }

    public CarType getCarType() {
        return carType;
    }

    public int getCarNumber() {
        return carNumber;
    }
}
