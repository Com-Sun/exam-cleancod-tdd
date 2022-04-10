package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import com.nhnacademy.exam.exceptions.CarDoesNotHaveEnoughMoneyException;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ParkingLotPaymentServiceTest {
    ParkingLotPaymentService parkingLotPaymentService;
    ParkingLotRepository parkingLotRepository;
    ParkingFee parkingFee = new ParkingFee(ParkingFeeStatus.WEEKDAY);
    Car car;


    @BeforeEach
    void setUp() {
        parkingLotRepository = mock(ParkingLotRepository.class);
        this.parkingLotPaymentService = new ParkingLotPaymentService(parkingLotRepository, parkingFee);
        car = new Car(CarType.SUV, 1838, new Money(Currency.WON, 50000));
    }

    @DisplayName("주차장에서 차가 나간다. 차가 나가려면 주차 시간만큼 결제해야 한다. 최초 30분일 경우 1000원")
    @Test
    void payParkingFeeTest() {
        int charge = parkingLotPaymentService.chargeParkingFeeToCar(car);
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
        assertThat(parkingLotPaymentService.chargeParkingFeeToCar(car)).isEqualTo(1500);
    }

    @DisplayName("50분 주차했을 경우 2000원")
    @Test
    void payParkingFeeTest3() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 50)));
        assertThat(parkingLotPaymentService.chargeParkingFeeToCar(car)).isEqualTo(2000);
    }

    @DisplayName("6시간 주차했을 경우 10000원")
    @Test
    void payParkingFeeTest4() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 1, 6, 0)));
        assertThat(parkingLotPaymentService.chargeParkingFeeToCar(car)).isEqualTo(10000);
    }

    @DisplayName("2일 연속 주차시 20000원")
    @Test
    void payParkingFeeTest5() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 2, 0, 1)));
        assertThat(parkingLotPaymentService.chargeParkingFeeToCar(car)).isEqualTo(20000);
    }

    @DisplayName("Car가 돈이 없을 시 나갈 수 없음")
    @Test
    void carCanNotExitIfMoneyIsNotEnoughTest() {
        Car poorCar = new Car(CarType.SUV, 1838, new Money(Currency.WON, 3000));
        when(parkingLotRepository.getHowLongCarIsParked(poorCar))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 2, 0, 1)));
        assertThatThrownBy(() -> parkingLotPaymentService.chargeParkingFeeToCar(poorCar))
            .isInstanceOf(CarDoesNotHaveEnoughMoneyException.class);
    }
}