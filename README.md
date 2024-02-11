# 지하철 노선도 미션
[ATDD 강의](https://edu.nextstep.camp/c/R89PYi5H) 실습을 위한 지하철 노선도 애플리케이션

# 구간 추가 요구사항 반영
***
## 사용자 스토리
````
사용자로서
지하철 노선도를 조금 더 편리하게 관리하기위해
위치에 상관없이 지하철 노선에 역을 추가 할 수 있다
````
## 기능 요구사항(완료 조건)
- 노선에 역 추가시 노선 가운데 추가 할 수 있다. 
- 노선에 역 추가시 노선 처음에 추가 할 수 있다.
- 이미 등록되어있는 역은 노선에 등록될 수 없다

## TODO
- [x] 시나리오 작성
  - 성공 시나리오
    ````
    Given 지하철 노선을 생성한다.
    Given 해당 지하철 노선은
          상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재한다.
    When 생성한 지하철 노선에 
          상행역 : 종로3가역 / 하행역이 종각역 / 길이가 4 인 구간을 등록하면
    Then 지하철 노선 조회 시,
          - 종로3가역 다음역은 종각역이고, 종각역 다음역은 시청역이다.
          - 종각역과 시청역 구간의 길이 = 2 이다.
    ````

    ````
    Given 종로3가역, 시청역 구간이 등록된 지하철 노선을 생성하고
    When 생성한 지하철 노선에 상행역이 종로5가역, 하행역이 종로3가역 명시된 구간을 등록하면
    Then 지하철 노선 조회 시,
          - 상행종점역은 종로5가역이다.
          - 노선 전체 길이가 등록한 구간의 길이만큼 늘어난다.
    ````
  - 실패 시나리오
    ````
    Given 종로3가역, 시청역 구간이 등록된 지하철 노선을 생성하고
    When 생성한 지하철 노선에 상행역이 동대문역, 하행역이 종로5가역으로 명시된 구간을 등록하면
    Then 등록되지 않고 코드값 400 (Bad Request) 을 반환한다.
    ````
    ````
    Given 지하철 노선을 생성한다.
    Given 해당 지하철 노선은
          상행역 : 종로3가역 / 하행역 : 시청역 / 길이 : 6 인 구간이 존재한다.
    When 생성한 지하철 노선에 
          상행역 : 종로3가역 / 하행역 : 종각역 / 길이가 6 이상인 구간을 등록하면
    Then 등록되지 않고 코드값 400 (Bad Request) 을 반환한다.
    ````
- [ ] 인수테스트 작성
- [ ] api 구현
- [ ] 단위테스트
  - [ ]객체 단위테스트
  - [ ]서비스 단위 테스트

