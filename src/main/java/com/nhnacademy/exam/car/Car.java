package com.nhnacademy.exam.car;

import com.nhnacademy.exam.parkingservice.CarDoesNotHaveEnoughMoneyException;

public class Car {
    CarType carType;
    int carNumber;
    Money money;

    public Car(CarType carType, int number, Money money) {
        this.carType = carType;
        this.carNumber = number;
        this.money = money;
    }

    public CarType getCarType() {
        return carType;
    }

    public int getCarNumber() {
        return carNumber;
    }

    public void payMoney(Money money) {
        if (this.money.amount - money.amount < 0) {
            throw new CarDoesNotHaveEnoughMoneyException("차가 충분한 돈을 갖고있지 않습니다.");
        }
        this.money.amount -= money.amount;
    }
}
