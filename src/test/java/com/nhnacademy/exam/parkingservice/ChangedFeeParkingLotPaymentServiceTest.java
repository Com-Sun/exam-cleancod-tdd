package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;

public class ChangedFeeParkingLotPaymentServiceTest {
    ParkingLotPaymentService parkingLotPaymentService;
    ParkingLotRepository parkingLotRepository;
    Car car = new Car(CarType.SUV, 3953, new Money(Currency.WON, 50000));
    ParkingFee parkingFee;

    @BeforeEach
    void setUp(){
        parkingLotRepository = mock(ParkingLotRepository.class);
        parkingLotPaymentService = new ParkingLotPaymentService(parkingLotRepository, new ParkingFee(ParkingFeeStatus.WEEKEND));
    }

    @DisplayName("주차장 요금 status가 제대로 변경되는지 확인")
    @Test
    void doesParkingFeeChangesTest(){
        assertThat(parkingLotPaymentService.parkingFee.getStatus()).isEqualTo(ParkingFeeStatus.WEEKEND);
        parkingLotPaymentService = new ParkingLotPaymentService(parkingLotRepository, new ParkingFee(ParkingFeeStatus.WEEKDAY));
        assertThat(parkingLotPaymentService.parkingFee.getStatus()).isEqualTo(ParkingFeeStatus.WEEKDAY);
    }

    @DisplayName("주차장 요금 변경 후 요금 청구가 제대로 되는지 확인")
    @Test
    void parkingFeeChangedTest() {
        int charge = parkingLotPaymentService.chargeParkingFeeToCar(car);
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 30)));
        assertThat(charge).isEqualTo(0);

        parkingLotPaymentService = new ParkingLotPaymentService(parkingLotRepository, new ParkingFee(ParkingFeeStatus.WEEKDAY));

        int charge2 = parkingLotPaymentService.chargeParkingFeeToCar(car);
        assertThat(charge2).isEqualTo(1000);
    }

    @DisplayName("1시간 1분일 경우")
    @Test
    void parkingFeeChangedTest2() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 1, 1)));
        int charge2 = parkingLotPaymentService.chargeParkingFeeToCar(car);
        assertThat(charge2).isEqualTo(1500);
    }

    @DisplayName(" 2시간 1분일 경우")
    @Test
    void parkingFeeChangedTest3() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 2, 1)));

        int charge2 = parkingLotPaymentService.chargeParkingFeeToCar(car);
        assertThat(charge2).isEqualTo(4500);
    }

    @DisplayName("일일 최대금액")
    @Test
    void parkingFeeChangedTest4() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 2, 0, 0)));

        int charge2 = parkingLotPaymentService.chargeParkingFeeToCar(car);
        assertThat(charge2).isEqualTo(15000);
    }

    @DisplayName("이틀 연속 주차")
    @Test
    void parkingFeeChangedTest5() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 2, 15, 0)));

        int charge2 = parkingLotPaymentService.chargeParkingFeeToCar(car);
        assertThat(charge2).isEqualTo(30000);
    }

    @DisplayName("경차의 경우 요금 50% 감면")
    @Test
    void lightCarParkingFeeTest(){
        Car lightCar = new Car(CarType.LIGHTCAR, 1356, new Money(Currency.WON, 50000));
        when(parkingLotRepository.getHowLongCarIsParked(lightCar))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 2, 15, 0)));

        int lightCarCharge = parkingLotPaymentService.chargeParkingFeeToCar(lightCar);
        assertThat(lightCarCharge).isEqualTo(15000);
    }

    @DisplayName("경차 요금 50%할인 시 0을 나누는 Exception 처리")
    @Test
    void lightCarParkingFeeTest2(){
        Car lightCar = new Car(CarType.LIGHTCAR, 1356, new Money(Currency.WON, 50000));
        when(parkingLotRepository.getHowLongCarIsParked(lightCar))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 1)));

        int lightCarCharge = parkingLotPaymentService.chargeParkingFeeToCar(lightCar);
        assertThat(lightCarCharge).isEqualTo(0);
    }
}
