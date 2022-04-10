package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import com.nhnacademy.exam.exceptions.CarDoesNotHaveEnoughMoneyException;
import com.nhnacademy.exam.exceptions.ParkingSpaceIsAlreadyUsedException;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingLotSystemTest {
    ParkingLotRepository parkingLotRepository;
    ParkingLotSystem parkingLotSystem;
    Car car;

    @BeforeEach
    void setUp() {
        parkingLotRepository = new ParkingLotRepository();
        parkingLotSystem = new ParkingLotSystem(parkingLotRepository);
        car = new Car(CarType.SUV, 1838, new Money(Currency.WON, 50000));
    }

    @DisplayName("차가 들어오면 번호판을 인식한다.")
    @Test
    void carNumberScanTest() {
        parkingLotSystem.scanCarNumber(car);
        assertThat(parkingLotRepository.getCarNumber(car)).isEqualTo(1838);

        // 메소드를 호출했는지
    }

    @DisplayName("A-1에 주차했을 경우 주차할 수 있는지의 여부를 확인한다.")
    @Test
    void carParkingTest() {
        parkingLotSystem.trackWhereCarIsParked(car, "A-1");
        assertThat(parkingLotSystem.isParkingSpaceAvailable("A-1")).isFalse();
    }

    @DisplayName("이미 주차된 구역에 주차를 시도했을 경우 예외처리")
    @Test
    void parkingAreaExceptionTest() {
        parkingLotSystem.trackWhereCarIsParked(car, "A-1");
        assertThatThrownBy(
            () -> parkingLotSystem.trackWhereCarIsParked(
                new Car(CarType.SUV, 1123, new Money(Currency.WON, 3000)), "A-1"))
            .isInstanceOf(ParkingSpaceIsAlreadyUsedException.class);
    }

}