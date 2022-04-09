# 개요

## ParkingLotService

- parkingSpace: 주차공간을 관리하는 Map<String, Boolean>타입 속성
- parkingLotRepository: 외부에서 주입

---

ParkingLotService에서 모든 주차에 관한 서비스 관리

- scanCarNumber()
- trackWhereCarIsParked()
- isParkingSpaceAvailable()
- chargeParkingFeeToCar()
  - calculateParkingFeeOfCar()

## ParkingLotRepository

- carInfo: 주차된 차의 정보를 관리하는 Map<Car, LocalDateTime>타입 속성
---

- getCarNumber()
- saveCarInfo()
- getHowLongCarIsParked()


# 기능 구현

## Spec 1

### 주차장에 차가 들어온다.
  - 번호판을 Scan하여 ParkingLotRepository에 저장하는 기능 구현
### A-1에 주차한다.
  - 만약 A-1이 사용중이라면 Throw Exception 기능 구현
  - 아니라면 A-1을 사용중인 상태로 만드는 기능 구현
### 주차장에서 차가 나간다.
  - 주차한 시간만큼 요금표에 따른 fee 청구 기능 구현
  - Car가 돈이 없을 시 Throw Exception 기능 구현 (나갈 수 없음)
  - TODO: 사용중이던 주차공간을 사용할 수 있는상태로 변화 - void 함수 NullPointerException 해결해야함

## Spec 2

- TODO: 쓰레드를 사용하여 동시성을 구현하라..?
- 위가 힘들면 강제로 n번째 입구로 보내버려라..?

### 주차장 입구가 n개입니다.
- ??

### 주차장 출구가 n개입니다.
- ??


## Spec 3

주차장 요금 변경
- TODO: 경차는 50% 할인
- TODO: 트럭은 주차 불가


## Spec4

- Payco인증서버를 만든 뒤 목킹
- Parameterized Test 를 사용해 볼 것
