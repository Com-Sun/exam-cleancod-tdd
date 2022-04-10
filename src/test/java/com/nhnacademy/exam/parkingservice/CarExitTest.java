package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;


import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CarExitTest {
    ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
    ParkingLotSystem parkingLotSystem = new ParkingLotSystem(parkingLotRepository);
    Car car = new Car(CarType.SUV, 1942, new Money(Currency.WON, 10000));

//    @DisplayName("Car가 주차장에서 나갈 시 ParkingSpace를 주차 가능하게 만든다.")
//    @Test
//    void makeParkingSpaceAvailableWhenCarExit() {
//        parkingLotSystem.scanCarNumber(car);
//        parkingLotSystem.trackWhereCarIsParked(car, "A-1");
//        assertThat(parkingLotSystem.isParkingSpaceAvailable("A-1")).isFalse();
//        parkingLotSystem.chargeParkingFeeToCar(car);
//        assertThat(parkingLotSystem.isParkingSpaceAvailable("A-1")).isTrue();
//    }
}
