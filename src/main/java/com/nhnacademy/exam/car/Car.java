package com.nhnacademy.exam.car;

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
