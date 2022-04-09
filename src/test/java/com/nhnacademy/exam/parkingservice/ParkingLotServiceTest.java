package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingLotServiceTest {
    ParkingLotRepository parkingLotRepository;
    ParkingLotService parkingLotService;
    Car car;

    @BeforeEach
    void setUp() {
        parkingLotRepository = mock(ParkingLotRepository.class);
        parkingLotService = new ParkingLotService(parkingLotRepository);
        car = new Car(CarType.SUV, 1838, new Money(Currency.WON, 50000));
    }

    @DisplayName("차가 들어오면 번호판을 인식한다.")
    @Test
    void carNumberScanTest() {
        parkingLotService.scanCarNumber(car);
        when(parkingLotRepository.getCarNumber(car)).thenReturn(car.getCarNumber());
        assertThat(parkingLotRepository.getCarNumber(car)).isEqualTo(1838);

        // 메소드를 호출했는지
    }

    @DisplayName("A-1에 주차했을 경우 주차할 수 있는지의 여부를 확인한다.")
    @Test
    void carParkingTest() {
        parkingLotService.trackWhereCarIsParked(car, "A-1");
        assertThat(parkingLotService.isParkingSpaceAvailable("A-1")).isFalse();
    }

    @DisplayName("이미 주차된 구역에 주차를 시도했을 경우 예외처리")
    @Test
    void parkingAreaExceptionTest() {
        parkingLotService.trackWhereCarIsParked(car, "A-1");
        assertThatThrownBy(
            () -> parkingLotService.trackWhereCarIsParked(
                new Car(CarType.SUV, 1123, new Money(Currency.WON, 3000)), "A-1"))
            .isInstanceOf(ParkingSpaceIsAlreadyUsedException.class);
    }

    @DisplayName("주차장에서 차가 나간다. 차가 나가려면 주차 시간만큼 결제해야 한다. 최초 30분일 경우 1000원")
    @Test
    void payParkingFeeTest() {
        int charge = parkingLotService.chargeParkingFeeToCar(car);
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 30)));
        assertThat(charge).isEqualTo(1000);
    }

    @DisplayName("30분 1초일 경우 1500원")
    @Test
    void payParkingFeeTest2() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 31)));
        assertThat(parkingLotService.chargeParkingFeeToCar(car)).isEqualTo(1500);
    }

    @DisplayName("50분 주차했을 경우 2000원")
    @Test
    void payParkingFeeTest3() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 50)));
        assertThat(parkingLotService.chargeParkingFeeToCar(car)).isEqualTo(2000);
    }

    @DisplayName("6시간 주차했을 경우 10000원")
    @Test
    void payParkingFeeTest4() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 1, 6, 0)));
        assertThat(parkingLotService.chargeParkingFeeToCar(car)).isEqualTo(10000);
    }

    @DisplayName("2일 연속 주차시 20000원")
    @Test
    void payParkingFeeTest5() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 2, 0, 1)));
        assertThat(parkingLotService.chargeParkingFeeToCar(car)).isEqualTo(20000);
    }

    @DisplayName("Car가 돈이 없을 시 나갈 수 없음")
    @Test
    void carCanNotExitIfMoneyIsNotEnough() {
        Car poorCar = new Car(CarType.SUV, 1838, new Money(Currency.WON, 3000));
        when(parkingLotRepository.getHowLongCarIsParked(poorCar))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 2, 0, 1)));
        assertThatThrownBy(() -> parkingLotService.chargeParkingFeeToCar(poorCar))
            .isInstanceOf(CarDoesNotHaveEnoughMoneyException.class);
    }
}