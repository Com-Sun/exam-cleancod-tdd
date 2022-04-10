package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.nhnacademy.exam.car.*;
import com.nhnacademy.exam.paycoserver.PaycoServer;
import java.time.Duration;
import java.time.LocalDateTime;
import org.junit.jupiter.api.*;

public class PaycoPaymentTest {
    ParkingLotPaymentService parkingLotPaymentService;
    ParkingLotRepository parkingLotRepository;
    PaycoServer paycoServer;
    Car car;
    User user;


    @BeforeEach
    void setUp() {
        parkingLotRepository = mock(ParkingLotRepository.class);
        paycoServer = mock(PaycoServer.class);
        parkingLotPaymentService = new ParkingLotPaymentService(parkingLotRepository,
            new ParkingFee(ParkingFeeStatus.WEEKDAY), paycoServer);
        car = new Car(CarType.SUV, 1579);
        user = new User(car, new Money(Currency.WON, 50000), "PAYCO");

    }

    @DisplayName("사용자가 PAYCO회원인 경우 10% 할인 테스트")
    @Test
    void paycoAuthenticationTest() {
        when(paycoServer.checkIsUserValid(any()))
            .thenReturn(true);
        when(parkingLotRepository.getHowLongCarIsParked(any()))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 30)));
        assertThat(paycoServer.checkIsUserValid(user)).isTrue();
        verify(paycoServer, times(1)).checkIsUserValid(user);
        assertThat(parkingLotPaymentService.chargeParkingFeeToUser(user)).isEqualTo(900);
    }

    @DisplayName("사용자가 PAYCO회원인 경우 10% 할인 테스트2")
    @Test
    void paycoAuthenticationTest2() {
        when(paycoServer.checkIsUserValid(any()))
            .thenReturn(true);
        when(parkingLotRepository.getHowLongCarIsParked(any()))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 2, 0)));
        assertThat(paycoServer.checkIsUserValid(user)).isTrue();
        verify(paycoServer, times(1)).checkIsUserValid(user);
        assertThat(parkingLotPaymentService.chargeParkingFeeToUser(user)).isEqualTo(9000);
    }

    @DisplayName("사용자 인증이 안될 경우 요금할인혜택이 없음")
    @Test
    void notAuthenticatedTest() {
        when(paycoServer.checkIsUserValid(any()))
            .thenReturn(false);
        when(parkingLotRepository.getHowLongCarIsParked(any()))
            .thenReturn(Duration.between(LocalDateTime.of(0, 1, 1, 0, 0),
                LocalDateTime.of(0, 1, 1, 0, 30)));
        assertThat(parkingLotPaymentService.chargeParkingFeeToUser(user)).isEqualTo(1000);
    }
}