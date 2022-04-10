package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import com.nhnacademy.exam.exceptions.ParkingSpaceIsAlreadyUsedException;
import com.nhnacademy.exam.exceptions.TruckCanNotParkException;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ParkingLotSystem {
    Map<String, Boolean> parkingSpace = new HashMap<>();
    ParkingLotRepository parkingLotRepository;
    ParkingFee parkingFee = new ParkingFee(ParkingFeeStatus.WEEKDAY);

    public ParkingLotSystem(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public ParkingLotSystem(ParkingLotRepository parkingLotRepository, ParkingFee parkingFee) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingFee = parkingFee;
    }

    public void scanCarNumber(Car car) {
        if (car.getCarType() == CarType.TRUCK) {
            throw new TruckCanNotParkException("대형 차는 주차할 수 없습니다.");
        }
        this.parkingLotRepository.saveCarInfo(car);
    }

    public void trackWhereCarIsParked(Car car, String code) {
        try {
            if (!parkingSpace.get(code)) {
                throw new ParkingSpaceIsAlreadyUsedException("이미 사용된 자리입니다.");
            }
        } catch (NullPointerException e) {
        }
        this.parkingLotRepository.saveWhereCarIsParked(car, code);
        parkingSpace.put(code, false);
    }

    public boolean isParkingSpaceAvailable(String code) {
        return parkingSpace.get(code);
    }

    public void makeParkingSpaceAvailable(Car car) {
        String code = parkingLotRepository.carParkedSpaceInfo.get(car);
        parkingSpace.put(code, true);
    }
}
