package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.Car;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ParkingLotRepository {
    Map<Car, LocalDateTime> carParkedTimeInfo = new HashMap<>();
    Map<Car, String> carParkedSpaceInfo = new HashMap<>();

    public int getCarNumber(Car car) {
        return car.getCarNumber();
    }

    public void saveCarInfo(Car car) {
        carParkedTimeInfo.put(car, LocalDateTime.now());
    }

    public Duration getHowLongCarIsParked(Car car) {
        return (Duration.between(carParkedTimeInfo.get(car), LocalDateTime.now()));
    }

    public void saveWhereCarIsParked(Car car, String code) {
        carParkedSpaceInfo.put(car, code);
    }
}

