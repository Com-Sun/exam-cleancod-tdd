package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.car.Currency;
import com.nhnacademy.exam.car.Money;
import com.nhnacademy.exam.car.User;
import com.nhnacademy.exam.exceptions.UserDoesNotHaveEnoughMoneyException;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;

class ParkingLotPaymentServiceTest {
    ParkingLotPaymentService parkingLotPaymentService;
    ParkingLotRepository parkingLotRepository;
    ParkingFee parkingFee = new ParkingFee(ParkingFeeStatus.WEEKDAY);
    Car car;
    User user;


    @BeforeEach
    void setUp() {
        parkingLotRepository = mock(ParkingLotRepository.class);
        this.parkingLotPaymentService = new ParkingLotPaymentService(parkingLotRepository, parkingFee);
        car = new Car(CarType.SUV, 1838);
        user = new User(car, new Money(Currency.WON, 50000), "PAYCO");
    }

    @DisplayName("주차장에서 차가 나간다. 차가 나가려면 주차 시간만큼 결제해야 한다. 최초 30분일 경우 1000원")
    @Test
    void payParkingFeeTest() {
        int charge = parkingLotPaymentService.chargeParkingFeeToUser(user);
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
        assertThat(parkingLotPaymentService.chargeParkingFeeToUser(user)).isEqualTo(1500);
    }

    @DisplayName("50분 주차했을 경우 2000원")
    @Test
    void payParkingFeeTest3() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 50)));
        assertThat(parkingLotPaymentService.chargeParkingFeeToUser(user)).isEqualTo(2000);
    }

    @DisplayName("6시간 주차했을 경우 10000원")
    @Test
    void payParkingFeeTest4() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 1, 6, 0)));
        assertThat(parkingLotPaymentService.chargeParkingFeeToUser(user)).isEqualTo(10000);
    }

    @DisplayName("2일 연속 주차시 20000원")
    @Test
    void payParkingFeeTest5() {
        when(parkingLotRepository.getHowLongCarIsParked(car))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 2, 0, 1)));
        assertThat(parkingLotPaymentService.chargeParkingFeeToUser(user)).isEqualTo(20000);
    }

    @DisplayName("User가 돈이 없을 시 나갈 수 없음")
    @Test
    void userCanNotExitIfMoneyIsNotEnoughTest() {
        User poorUser = new User(car, new Money(Currency.WON, 1000), "SS");
        when(parkingLotRepository.getHowLongCarIsParked(user.getCar()))
            .thenReturn(
                Duration.between(LocalDateTime.of(0, 1, 1, 0, 0), LocalDateTime.of(0, 1, 2, 0, 1)));
        assertThatThrownBy(() -> parkingLotPaymentService.chargeParkingFeeToUser(poorUser))
            .isInstanceOf(UserDoesNotHaveEnoughMoneyException.class);
    }
}