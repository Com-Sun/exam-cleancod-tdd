package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;


import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import com.nhnacademy.exam.car.User;
import com.nhnacademy.exam.paycoserver.PaycoServer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CarExitTest {
    ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
    ParkingLotParkingService parkingLotParkingService = new ParkingLotParkingService(parkingLotRepository);
    ParkingLotPaymentService parkingLotPaymentService = new ParkingLotPaymentService(parkingLotRepository, new ParkingFee(ParkingFeeStatus.WEEKDAY), new PaycoServer());
    Car car = new Car(CarType.SUV, 1942);
    User user = new User(car, new Money(Currency.WON, 50000), "PAYCO");

    @DisplayName("Car가 주차장에서 나갈 시 ParkingSpace를 주차 가능하게 만든다.")
    @Test
    void makeParkingSpaceAvailableWhenCarExit() {
        parkingLotParkingService.scanCarNumber(car);
        parkingLotParkingService.trackWhereCarIsParked(car, "A-1");
        assertThat(parkingLotParkingService.isParkingSpaceAvailable("A-1")).isFalse();
        parkingLotPaymentService.chargeParkingFeeToUser(user);
        assertThat(parkingLotParkingService.isParkingSpaceAvailable("A-1")).isTrue();
    }
}
