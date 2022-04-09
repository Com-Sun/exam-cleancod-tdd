## ParkingLot

### ParkingLotService

- ParkingLotRepository parkinglotRepository;
- 외부에서 인자로 의존성 주입

- scan()
  - saveCarNumber(number)
  - 스캔서비스는 Repository에 의존함

- park()
  - ParkingLotRepository parkinglotRepository;
  - 주차 서비스는 Repository에 의존함

- pay()
  - 결제 서비스는 Repository에 의존함


### ParkingLotRepository




```
    private final AccountRepository repository;

    public PaymentService(AccountRepository repository) {
        this.repository = repository;
    }

```

## Car




## Spec4

Payco인증서버를 만든 뒤 목킹

java time 패키지

private 메소드는 테스트하는것이 아님. 설게를 잘못 한 것. 꼼수: package private으로 만들면 테스트는 가능
private메소드를 호출할 정도면 안됨. 클래스분리, 메소드 분리를 해야함.

시간 - 하나씩 하나씩 구현.
한땀 한땀 만들기.
- Parameterized Test 를 사용해 볼 것
