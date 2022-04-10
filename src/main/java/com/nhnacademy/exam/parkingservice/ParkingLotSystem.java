package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.exceptions.ParkingSpaceIsAlreadyUsedException;
import com.nhnacademy.exam.exceptions.TruckCanNotParkException;

public class ParkingLotSystem {
    ParkingLotRepository parkingLotRepository;

    public ParkingLotSystem(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public void scanCarNumber(Car car) {
        if (car.getCarType() == CarType.TRUCK) {
            throw new TruckCanNotParkException("대형 차는 주차할 수 없습니다.");
        }
        this.parkingLotRepository.saveCarInfo(car);
    }

    public void trackWhereCarIsParked(Car car, String code) {
        try {
            if (!parkingLotRepository.parkingSpace.get(code)) {
                throw new ParkingSpaceIsAlreadyUsedException("이미 사용된 자리입니다.");
            }
        } catch (NullPointerException e) {
        }
        this.parkingLotRepository.saveWhereCarIsParked(car, code);
        parkingLotRepository.parkingSpace.put(code, false);
    }

    public boolean isParkingSpaceAvailable(String code) {
        return parkingLotRepository.parkingSpace.get(code);
    }

}
