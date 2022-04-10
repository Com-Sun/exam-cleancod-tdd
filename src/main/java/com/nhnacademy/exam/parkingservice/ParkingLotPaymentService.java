package com.nhnacademy.exam.parkingservice;

import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import com.nhnacademy.exam.car.User;
import java.time.Duration;

public class ParkingLotPaymentService {
    ParkingLotRepository parkingLotRepository;
    ParkingFee parkingFee;

    public static final int TENMINUTE = 600;
    public static final int THIRTYMINUTE = 1800;
    public static final int ONEHOUR = 3600;
    public static final int ONEDAY = 86400;

    public ParkingLotPaymentService(ParkingLotRepository parkingLotRepository,
                                    ParkingFee parkingFee) {
        this.parkingLotRepository = parkingLotRepository;
        this.parkingFee = parkingFee;
    }

    public int chargeParkingFeeToUser(User user) {
        Duration duration = parkingLotRepository.getHowLongCarIsParked(user.getCar());
        if (this.parkingFee.getStatus() == ParkingFeeStatus.WEEKDAY) {
            int amount = calculateParkingFeeOfCarWeekday(duration);
            Money money = new Money(Currency.WON, amount);
            user.payMoney(money);
            try {
                String code = parkingLotRepository.carParkedSpaceInfo.get(user.getCar());
                parkingLotRepository.parkingSpace.put(code, true);
            } catch (NullPointerException e) {
            }
            return amount;
        }

        int amount = calculateParkingFeeOfCarWeekend(duration);
        if (user.getCar().getCarType() == CarType.LIGHTCAR) {
            try {
                amount = amount / 2;
            } catch (ArithmeticException e) {
                System.out.println("0인 경우는 어쩔수 없음");
            }
        }
        Money money = new Money(Currency.WON, amount);
        user.payMoney(money);

        try {
            String code = parkingLotRepository.carParkedSpaceInfo.get(user.getCar());
            parkingLotRepository.parkingSpace.put(code, true);

        } catch (NullPointerException e) {

        }
        return amount;
    }

    private int calculateParkingFeeOfCarWeekend(Duration duration) {
        if (duration.getSeconds() <= THIRTYMINUTE) {
            return 0;
        }
        if (duration.getSeconds() <= ONEHOUR) {
            return 1000;
        }
        if (duration.getSeconds() <= TENMINUTE * 34) {
            if (duration.getSeconds() % TENMINUTE > 0) {
                return (int) (((duration.getSeconds() / TENMINUTE) + 1) * 500) - 2000;
            }
            return (int) ((duration.getSeconds() / TENMINUTE) * 500) - 2000;
        }
        if (duration.getSeconds() > ONEDAY) {
            int fee = (int) (duration.getSeconds() / ONEDAY) * 15000;
            return fee + 15000;
        }
        return 15000;
    }

    private int calculateParkingFeeOfCarWeekday(Duration duration) {
        if (duration.getSeconds() <= THIRTYMINUTE) {
            return 1000;
        }
        if (duration.getSeconds() <= THIRTYMINUTE * 3) {
            if (duration.getSeconds() % TENMINUTE > 0) {
                return (int) (((duration.getSeconds() / TENMINUTE) + 1) * 500) - 500;
            }
            return (int) ((duration.getSeconds() / TENMINUTE) * 500) - 500;
        }
        if (duration.getSeconds() > ONEDAY) {
            int fee = (int) (duration.getSeconds() / ONEDAY) * 10000;
            return fee + 10000;
        }
        return 10000;
    }
}
