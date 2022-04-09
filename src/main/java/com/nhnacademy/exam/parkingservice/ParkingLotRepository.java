package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.Car;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class ParkingLotRepository {
    Map<Car, LocalDateTime> carInfo = new HashMap<>();

    public int getCarNumber(Car car) {
        //TODO: 레포지토리에서 일치하는 번호판을 찾아 리턴
        return car.getCarNumber();
    }

    public void saveCarInfo(Car car) {
        carInfo.put(car, LocalDateTime.now());
    }

    public Duration getHowLongCarIsParked(Car car) {
        return (Duration.between(carInfo.get(car), LocalDateTime.now()));
    }

}

