package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.exceptions.ParkingSpaceIsAlreadyUsedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingLotParkingServiceTest {
    ParkingLotRepository parkingLotRepository;
    ParkingLotParkingService parkingLotParkingService;
    Car car;

    @BeforeEach
    void setUp() {
        parkingLotRepository = new ParkingLotRepository();
        parkingLotParkingService = new ParkingLotParkingService(parkingLotRepository);
        car = new Car(CarType.SUV, 1838);
    }

    @DisplayName("차가 들어오면 번호판을 인식한다.")
    @Test
    void carNumberScanTest() {
        parkingLotParkingService.scanCarNumber(car);
        assertThat(parkingLotRepository.getCarNumber(car)).isEqualTo(1838);

        // 메소드를 호출했는지
    }

    @DisplayName("A-1에 주차했을 경우 주차할 수 있는지의 여부를 확인한다.")
    @Test
    void carParkingTest() {
        parkingLotParkingService.trackWhereCarIsParked(car, "A-1");
        assertThat(parkingLotParkingService.isParkingSpaceAvailable("A-1")).isFalse();
    }

    @DisplayName("이미 주차된 구역에 주차를 시도했을 경우 예외처리")
    @Test
    void parkingAreaExceptionTest() {
        parkingLotParkingService.trackWhereCarIsParked(car, "A-1");
        assertThatThrownBy(
            () -> parkingLotParkingService.trackWhereCarIsParked(
                new Car(CarType.SUV, 1123), "A-1"))
            .isInstanceOf(ParkingSpaceIsAlreadyUsedException.class);
    }

}