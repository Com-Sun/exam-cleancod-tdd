package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.exceptions.CarCannotEnterOverEntranceNumber;
import com.nhnacademy.exam.exceptions.ParkingSpaceIsAlreadyUsedException;
import com.nhnacademy.exam.exceptions.TruckCanNotParkException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ParkingLotParkingService {
    ParkingLotRepository parkingLotRepository;

    public ParkingLotParkingService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public void scanCarNumber(Car car) {
        if (car.getCarType() == CarType.TRUCK) {
            throw new TruckCanNotParkException("대형 차는 주차할 수 없습니다.");
        }
        this.parkingLotRepository.saveCarInfo(car);
    }

    public void scanCarsNumber(ArrayList<Car> cars) {
        int count = 0;
        List<LocalDateTime> temp = new ArrayList<>();
        for (Car car : cars) {
            this.parkingLotRepository.saveCarInfo(car);
            temp.add(parkingLotRepository.carParkedTimeInfo.get(car));
        }
        for (LocalDateTime localDateTime : temp) {
            for (Car car: cars) {
                if (parkingLotRepository.carParkedTimeInfo.get(car).equals(localDateTime)) {
                    count++;
                    break;
                }
            }
        }
        if (count >= 4) {
            throw new CarCannotEnterOverEntranceNumber("동시에 입구의 수보다 많은 차가 들어올 수 없습니다.");
        }
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
