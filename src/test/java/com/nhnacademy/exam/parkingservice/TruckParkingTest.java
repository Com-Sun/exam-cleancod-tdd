package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import com.nhnacademy.exam.exceptions.TruckCanNotParkException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TruckParkingTest {
    ParkingLotRepository parkingLotRepository = new ParkingLotRepository();
    ParkingLotService parkingLotService = new ParkingLotService(parkingLotRepository);

    @DisplayName("주차장에 대형차는 주차할 수 없음")
    @Test
    void TruckCanNotParkTest() {
        Car truck = new Car(CarType.TRUCK, 1868, new Money(Currency.WON, 30000));
        assertThatThrownBy(() -> parkingLotService.scanCarNumber(truck))
            .isInstanceOf(TruckCanNotParkException.class)
            .hasMessageContaining("주차할 수 없습니다.");

        assertThat(parkingLotRepository.carParkedTimeInfo.get(truck)).isNull();

        Car car = new Car(CarType.SUV, 1234, new Money(Currency.WON, 1000));
        parkingLotService.scanCarNumber(car);
        assertThat(parkingLotRepository.carParkedTimeInfo.get(car)).isNotNull();

    }
}
