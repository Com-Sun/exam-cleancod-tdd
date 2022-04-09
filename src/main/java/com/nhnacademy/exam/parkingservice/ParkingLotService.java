package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ParkingLotService {
    Map<String, Boolean> parkingSpace = new HashMap<>();
    ParkingLotRepository parkingLotRepository;
    ParkingFee parkingFee = new ParkingFee(ParkingFeeStatus.WEEKDAY);

    public ParkingLotService(ParkingLotRepository parkingLotRepository) {
        this.parkingLotRepository = parkingLotRepository;
    }

    public ParkingLotService(ParkingLotRepository parkingLotRepository, ParkingFee parkingFee) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingFee = parkingFee;
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
        this.parkingLotRepository.saveWhereCarIsParked(car, code);
        parkingSpace.put(code, false);
    }

    public boolean isParkingSpaceAvailable(String code) {
        return parkingSpace.get(code);
    }

    public int chargeParkingFeeToCar(Car car) {
        Duration duration = parkingLotRepository.getHowLongCarIsParked(car);
        if (this.parkingFee.getStatus() == ParkingFeeStatus.WEEKDAY) {
            int amount = calculateParkingFeeOfCarWeekday(duration);
            Money money = new Money(Currency.WON, amount);
            car.payMoney(money);
            return amount;
        }

        int amount = calculateParkingFeeOfCarWeekend(duration);
        if (car.getCarType() == CarType.LIGHTCAR) {
            try {
                amount = amount / 2;
            } catch (ArithmeticException e) {
                System.out.println("0인 경우는 어쩔수 없음");
            }
        }
        Money money = new Money(Currency.WON, amount);
        car.payMoney(money);
        return amount;
    }

    private int calculateParkingFeeOfCarWeekend(Duration duration) {
        if (duration.getSeconds() <= 1800L) {
            return 0;
        }
        if (duration.getSeconds() <= 3600L) {
            return 1000;
        }
        if (duration.getSeconds() <= 20400) {
            if (duration.getSeconds() % 600 > 0) {
                return (int) (((duration.getSeconds() / 600) + 1) * 500) - 2000;
            }
            return (int) ((duration.getSeconds() / 600) * 500) - 2000;
        }
        if (duration.getSeconds() > 86400) {
            int fee = (int) (duration.getSeconds() / 86400) * 15000;
            return fee + 15000;
        }
        return 15000;
    }

    private int calculateParkingFeeOfCarWeekday(Duration duration) {
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

    public void makeParkingSpaceAvailable(Car car) {
        String code = parkingLotRepository.carParkedSpaceInfo.get(car);
        parkingSpace.put(code, true);
    }
}
