package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ParkingLotService {
    Map<String, Boolean> parkingSpace = new HashMap<>();
    ParkingLotRepository parkingLotRepository;

    public ParkingLotService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public void scanCarNumber(Car car) {
        this.parkingLotRepository.saveCarInfo(car);
    }

    public void trackWhereCarIsParked(Car car, String code) {
        try {
            if (!parkingSpace.get(code)) {
                throw new ParkingSpaceIsAlreadyUsedException("이미 사용된 자리입니다.");
            }
        } catch (NullPointerException e) {
        }
        parkingSpace.put(code, false);
    }

    public boolean isParkingSpaceAvailable(String code) {
        return parkingSpace.get(code);
    }

    public int chargeParkingFeeToCar(Car car) {
        Duration duration = parkingLotRepository.getHowLongCarIsParked(car);
        int amount = calculateParkingFeeOfCar(duration);
        Money money = new Money(Currency.WON, amount);
        car.payMoney(money);
        return amount;
    }

    private int calculateParkingFeeOfCar(Duration duration) {
        if (duration.getSeconds() <= 1800L) {
            return 1000;
        }
        if (duration.getSeconds() <= 5400L) {
            if (duration.getSeconds() % 600 > 0) {
                return (int) (((duration.getSeconds() / 600) + 1) * 500) - 500;
            }
            return (int) ((duration.getSeconds() / 600) * 500) - 500;
        }
        if (duration.getSeconds() > 86400) {
            int fee = (int) (duration.getSeconds() / 86400) * 10000;
            return fee + 10000;
        }
        return 10000;
    }
}
