### 시퀀스 다이어그램

##### 1 토큰 검증 및 토큰 발급 후 대기열 등록

1️⃣ 주요 유저 대기열 토큰 기능
```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 토큰 검증 API
    participant Token as 토큰 서비스
    participant Queue as 대기열

    Client ->> API: 사용자정보로 토큰 조회 요청

    API ->> Token: 토큰 조회 요청

    alt 유효한 토큰
		    Token -->> API: 유효한 토큰 반환
        API -->> Client: 유효한 토큰 반환
    else 토큰 정보 없음
        Token ->> Queue: 신규 토큰 발급 후 대기열 등록 요청
        Queue -->> Token: 대기열 등록 완료 응답
        Token -->> API: 새로운 토큰 반환
        API -->> Client: 새로운 토큰 반환
    end

    loop 대기 상태 확인 (폴링)
        API ->> Queue: 대기 상태 확인 요청
        Queue -->> API: 최신 대기열 상태 응답
        API -->> Client: 대기열 상태 응답
    end

```
2️⃣**`기본` 예약 가능 날짜 / 좌석 API**

##### 2-1 콘서트 일정 조회

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 일정 요청 API
    participant Token as 토큰 서비스
    participant Date as 일정 서비스
		
    Client ->> API: 예약 가능 날짜 조회 요청
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)
		
		alt 유효하지 않은 토큰
	      API -->> Client: 토큰 에러 응답
		
    else 유효한 토큰
        API ->> Date: 예약 가능한 날짜 요청
        Date -->> API: 예약 가능한 날짜 목록 반환
        API -->> Client: 예약 가능한 날짜 목록 전달
    end
```

##### 2-2 콘서트 좌석 조회

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 좌석 요청 API
    participant Token as 토큰 서비스
    participant Seat as 좌석 서비스
	
    Client ->> API: 선택한 날짜의 좌석 조회 요청
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
	      API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
	      API ->> Seat: 선택한 날짜의 좌석 정보 요청
	      Seat -->> API: 선택한 날짜의 좌석 목록 반환
	      API -->> Client: 좌석 목록 전달
    end

```
3️⃣주요 좌석 예약 요청 API

##### 3 콘서트 예약 신청 및 임시예약

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 좌석 예약 API
    participant Token as 토큰 서비스
    participant Seat as 좌석 서비스
    participant Payment as 결제 서비스
	
    Client ->> API: 좌석 예약 요청 (날짜, 좌석 정보)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
        API ->> Seat: 좌석 예약 요청
        Seat -->> API: 좌석 예약 성공 응답 (임시 배정 정보 포함)
        API ->> Payment: 결제 요청 (임시 배정된 좌석)
        
        loop 결제 대기
            Payment -->> API: 결제 완료 상태 응답
            alt 결제 완료
                API -->> Client: 예약 완료 응답
            else 결제 실패 또는 시간 초과
                API ->> Seat: 임시 배정 해제 요청
                Seat -->> API: 임시 배정 해제 완료 응답
                API -->> Client: 예약 실패 응답
            end
        end
    end

```
4️⃣기본 잔액 충전 / 조회 API
##### 4-1 포인트 잔액 조회

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 잔액 조회 API
    participant Token as 토큰 서비스
    participant Balance as 잔액 서비스
	
    Client ->> API: 잔액 조회 요청 (사용자 ID)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
        API ->> Balance: 잔액 조회 요청 (사용자 ID)
        Balance -->> API: 현재 잔액 응답
        API -->> Client: 잔액 정보 전달
    end

```

##### 4-2 포인트 충전

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 잔액 충전 API
    participant Token as 토큰 서비스
    participant Balance as 잔액 서비스
	
    Client ->> API: 잔액 충전 요청 (사용자 ID, 충전 금액)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰    
        API ->> Balance: 잔액 충전 요청 (사용자 ID, 충전 금액)
        Balance -->> API: 잔액 충전 성공 응답
        API -->> Client: 잔액 충전 완료 응답
    end

```

5️⃣주요 결제 API
##### 5 포인트를 사용한 결제 및 대기열 삭제

```mermaid
sequenceDiagram
    actor Client as 사용자
    participant API as 결제 API
    participant Token as 토큰 서비스
    participant Payment as 결제 서비스
    participant Seat as 좌석 서비스
    participant Queue as 대기열 서비스

    Client ->> API: 결제 요청 (좌석 정보, 결제 금액)
    API ->> Token: 토큰 상태 확인 요청
    Token -->> API: 토큰 상태 응답 (유효한 토큰 여부)

    alt 유효하지 않은 토큰
        API -->> Client: 토큰 에러 응답
    else 유효한 토큰
        API ->> Payment: 결제 처리 요청 (좌석 정보, 결제 금액)
        Payment -->> API: 결제 완료 응답

        alt 결제 실패
            API -->> Client: 결제 실패 응답

        else 결제 성공
            API ->> Seat: 좌석 소유권 배정 요청 (사용자 ID, 좌석 정보)
            Seat -->> API: 소유권 배정 완료 응답
            API ->> Queue: 대기열 토큰 만료 요청 (사용자 ID)
            Queue -->> API: 대기열 토큰 만료 완료 응답
            API -->> Client: 결제 완료 및 예약 성공 응답
        end
    end
```
