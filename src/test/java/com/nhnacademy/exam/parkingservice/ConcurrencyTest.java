package com.nhnacademy.exam.parkingservice;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.nhnacademy.exam.car.Car;
import com.nhnacademy.exam.car.CarType;
import com.nhnacademy.exam.exceptions.CarCannotEnterOverEntranceNumber;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ConcurrencyTest {
    ParkingLotRepository parkingLotRepository;
    ParkingLotParkingService parkingLotParkingService;
    ArrayList<Car> cars = new ArrayList<>();

    @BeforeEach
    void setUp() {
        parkingLotRepository = new ParkingLotRepository();
        parkingLotParkingService = new ParkingLotParkingService(parkingLotRepository);
        for (int i = 0; i < 5; i++) {
            cars.add(new Car(CarType.SUV, i + 1000));
        }
    }

    @DisplayName("동시에 scan 이벤트가 4번 이상 발생할 경우 예외를 던지는지 테스트")
    @Test
    void ifFiveCarEntersConcurrencyTest() {
        assertThatThrownBy(() -> parkingLotParkingService.scanCarsNumber(cars))
            .isInstanceOf(CarCannotEnterOverEntranceNumber.class);
    }

    @DisplayName("동시에 scan이벤트가 3번 발생할 경우 Repository에 정보가 잘 저장되는지 테스트")
    @Test
    void ifThreeCarEntersConcurrencyTest() {
        ArrayList<Car> cars2 = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            cars2.add(new Car(CarType.SUV, i + 1000));
        }
        parkingLotParkingService.scanCarsNumber(cars2);
        for (Car car : cars2) {
            assertThat(parkingLotRepository.carParkedTimeInfo.get(car)).isNotNull();
        }
    }
}
