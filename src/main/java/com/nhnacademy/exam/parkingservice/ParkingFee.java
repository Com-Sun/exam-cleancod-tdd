package com.nhnacademy.exam.parkingservice;

public class ParkingFee {
    ParkingFeeStatus status;

    public ParkingFee(ParkingFeeStatus status) {
        this.status = status;
    }

    public ParkingFeeStatus getStatus() {
        return status;
    }

}
