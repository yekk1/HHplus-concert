# 🚢 HHPLUS BE06
콘서트 예약 서비스

## Milestone
github Issue & Project로 관리합니다.
![milestone img](./src/docs/Milestone%202024-10-09%20오전%202.20.11.png)
위 마일스톤은 2024.10.09 버전으로 과제 진행에 따라 유동적으로 변화할 수 있습니다. 
업데이트 된 버전은
[Link](https://github.com/users/yekk1/projects/1)
를 참고하세요.

## 시퀀스다이어그램
### 대기열 확인
```mermaid
sequenceDiagram
    participant 사용자
    participant 대기열시스템
    
    사용자->>대기열시스템: 대기열 확인 요청
    대기열시스템->>대기열시스템: 토큰 확인
    alt 토큰 없음
        대기열시스템-->>사용자: 토큰 발급
    else 토큰 존재
        대기열시스템-->>사용자: 기존 토큰 반환
    else 토큰 만료
        대기열시스템-->>사용자: 대기열 취소
    end
```

### 좌석 조회
```mermaid
sequenceDiagram
    participant 사용자
    participant 대기열시스템
    participant 예약시스템
    
    사용자->>대기열시스템: 예약 가능한 날짜 요청
    대기열시스템->>대기열시스템: 토큰 검증
    대기열시스템->>예약시스템: 예약 가능한 날짜 요청
    예약시스템-->>사용자: 예약 가능한 날짜 목록 응답
    
    사용자->>대기열시스템: [날짜]의 예약 가능한 좌석 요청
    대기열시스템->>대기열시스템: 토큰 검증
    대기열시스템->>예약시스템: [날짜]의 예약 가능한 좌석 요청
    예약시스템-->>사용자: 예약 가능한 좌석 목록 응답
```

### 좌석 예약
```mermaid
sequenceDiagram
    participant 사용자
    participant 대기열시스템
    participant 예약시스템
    
    사용자->>대기열시스템: 좌석 예약 요청
    대기열시스템->>대기열시스템: 토큰 검증

    대기열시스템->>예약시스템: 좌석 예약 요청
    alt 선점되지 않은 좌석일 때
        예약시스템-->>예약시스템: 좌석 5분간 임시 배정
        예약시스템-->>사용자: 좌석 예약 성공 응답
        
    else 선점 된 좌석일 때
        예약시스템-->>사용자: 좌석 예약 살패 응답
        
    end
```

### 좌석 결제
```mermaid
sequenceDiagram
    participant 사용자
    participant 예약시스템
    participant 결제시스템
    participant 잔액시스템
    
    사용자->>예약시스템: 예약 상태 요청
    예약시스템-->>사용자: 예약 상태 응답

    사용자->>예약시스템: 좌석 결제 요청
    예약시스템->>예약시스템: 예약 상태 확인
    alt 예약상태일 때
        예약시스템->>결제시스템: 좌석 결제 요청
        결제시스템->>잔액시스템: 잔액 조회 요청
        잔액시스템->>잔액시스템: 잔액 확인
        alt 잔액 존재 
            잔액시스템-->>결제시스템: 정상 응답
            결제시스템->>결제시스템: 결제 처리 및 전표 작성
            결제시스템-->>예약시스템: 예약 해제 요청
            예약시스템->>결제시스템: 예약 해제 응답
            결제시스템-->>사용자: 결제 처리 응답
        else 잔액 부족
            잔액시스템-->>결제시스템: 잔액 부족 응답
            결제시스템-->>사용자: 잔액 부족 응답
        end
    else 예약상태가 아닐 때
        예약시스템-->>사용자: 예약 불가 응답
    end
```

### 잔액 충전/조회
```mermaid
sequenceDiagram
    participant 사용자
    participant 잔액시스템
    
    사용자->>잔액시스템: 잔액 조회 요청
    잔액시스템-->>사용자: 잔액 응답
    
    사용자->>잔액시스템: 잔액 충전 요청
    잔액시스템->>사용자: 잔액 충전 응답
```

## ERD
![ERD](./src/docs/HHPlus_concert_ERD.png)

## 기술 스택
- Java Spring Boot
- DB: MySQl + Redis
- API Docs: Swagger
- JPA, jwt ...
## 패키지 구조
```
api/
  <도메인>/ (concert, point, queue)
    controller.java
    request/
    response/
application/
  <도메인>/
    facade.java
domain/
  <도메인>/ 
    usecase/
    entity/
infra/
  <도메인>/
common
  config/
```
