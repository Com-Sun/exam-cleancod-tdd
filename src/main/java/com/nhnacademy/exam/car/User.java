package com.nhnacademy.exam.car;

import com.nhnacademy.exam.exceptions.UserDoesNotHaveEnoughMoneyException;

public class User {
    Car car;
    Money money;
    String Id;

    public User(Car car, Money money, String id) {
        this.car = car;
        this.money = money;
        Id = id;
    }

    public Car getCar() {
        return car;
    }

    public Money getMoney() {
        return money;
    }

    public String getId() {
        return Id;
    }

    public void payMoney(Money money) {
        if (this.money.amount - money.amount < 0) {
            throw new UserDoesNotHaveEnoughMoneyException("차가 충분한 돈을 갖고있지 않습니다.");
        }
        this.money.amount -= money.amount;
    }
}
