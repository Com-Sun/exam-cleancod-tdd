# 개요

## ParkingLotService

- parkingSpace: 주차공간을 관리하는 Map<String, Boolean>타입 속성
- ParkingLotRepository: 외부에서 주입
- ParkingFee: 평소엔 WEEKDAY 요금제 적용. WEEKEND 요금으로 변경 가능

---

ParkingLotService에서 모든 주차에 관한 서비스 관리

## ParkingLotRepository

- carParkedTimeInfo: 주차된 차의 시간정보를 관리하는 Map<Car, LocalDateTime>타입 속성
- carParkedSpaceInfe: 주차된 차의 공간정보를 관리하는 Map<Car, String>타입 속성

---

ParkingLotRepository에선 주차된 차에 관한 정보 관리


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
  - 사용중이던 주차공간을 사용할 수 있는상태로 변경 기능 구현

## Spec 2

- TODO: 쓰레드를 사용하여 동시성을 구현하라..?
- 위가 힘들면 강제로 n번째 입구로 보내버려라..?

### 주차장 입구가 n개입니다.
- ??

### 주차장 출구가 n개입니다.
- ??


## Spec 3

### 주차장 요금표 변경 기능 구현
- ParkingFee 속성을 통해 평소엔 WEEKDAY 요금 적용
- WEEKEND 요금 적용시 다른 요금표 적용
- 경차는 50% 할인 기능 구현
- 트럭 타입 Car객체가 처음 Scan 될 시 Throw Exception 기능 구현


## Spec4

### 사용자가 Payco 회원일 경우 주차요금이 10% 할인

- TODO: Payco 회원 인증 테스트
- TODO: 회원 인증 시 주차요금 10% 할인
- TODO: Payco 인증이 안될 경우, 나머지 모두 다 익명사용자
- TODO: 익명사용자의 경우 할인혜택 없음

### 시간주차권 존재

- TODO: 주차권 기능 구현
