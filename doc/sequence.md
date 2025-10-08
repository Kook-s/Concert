# Sequence Diagram
## 유저 토큰 발급
``` mermaid
sequenceDiagram
	autonumber
	
	actor User
	participant API	
	participant Queue
	participant Token
	participant Database
	
	User ->> +API: 토큰 발급 요청
	API ->> +Queue: 대기열 위치 요청
	Queue ->> +Database: 현재 대기열 상태 조회
	Database -->> -Queue: 대기열 상태 반환
	Queue -->> -API: 대기열 위치 반환
	API ->> +Token: 토큰 생성 요청
	Token ->> +Database: 토큰 저장
	Database -->> -Token: 토큰 저장 완료
	Token -->> -API: 토큰 반환
	API -->> -User: 토큰 및 대기열 정보 반환
```

## 예약 가능 날짜 조회
``` mermaid
sequenceDiagram
	autonumber
	
	actor User
	participant API
	participant Token
	participant Concert
	participant Database
	
	User ->> +API: 예약 가능 날짜 조회 요청
	API ->> +Token: 토큰 유효성 검증
	Token ->> +Database: 토큰 조회
	Database -->> -Token: 토큰 반환
	Token -->> -API: 토큰 검증 결과
	alt 유효한 토큰
	API ->> +Concert: 예약 가능 날짜 요청
	Concert ->> +Database: 날짜 조회
	Database -->> -Concert: 날짜 반환
	Concert -->> -API: 예약 가능 날짜 목록 반환
	API -->> -User: 예약 가능 날짜 목록 반환
	else 유효하지 않은 토큰
	API -->> User: 오류 메시지 반환
	end
```

## 예약 가능 좌석 조회
``` mermaid
sequenceDiagram
	autonumber
	
	actor User
	participant API
	participant Token
	participant Seat
	participant Database
	
	User ->> +API: 예약 가능 좌석 조회 요청
	API ->> +Token: 토큰 유효성 검증
	Token ->> +Database: 토큰 조회
	Database -->> -Token: 토큰 반환
	Token -->> -API: 토큰 검증 결과
	alt 유효한 토큰
	API ->> +Seat: 예약 가능 좌석 요청
	Seat ->> +Database: 좌석 조회
	Database -->> -Seat: 좌석 반환
	Seat -->> -API: 예약 가능 좌석 목록 반환
	API -->> -User: 예약 가능 좌석 목록 반환
	else 유효하지 않은 토큰
	API -->> User: 오류 메시지 반환
	end
```

## 잔액 조회
``` mermaid
sequenceDiagram
	autonumber
	
	actor User
	participant API
	participant Payment
	participant Database
	
	User ->> +API: 잔액 조회 요청
	API ->> +Payment: 잔액 조회 요청
	Payment ->> +Database: 잔액 조회
	Database -->> -Payment: 잔액 반환
	Payment -->> -API: 잔액 반환
	API -->> -User: 잔액 반환
```

## 잔액 충전
``` mermaid
sequenceDiagram
	autonumber
	
	actor User
	participant API
	participant Payment
	participant Database
	
	User ->> +API: 잔액 충전 요청
	API ->> +Payment: 잔액 충전 요청
	Payment ->> +Database: 잔액 변경
	Database -->> -Payment: 잔액 변경
	Payment -->> -API: 잔액 반환
	API -->> -User: 잔액 반환
```

## 좌석 예약
``` mermaid
sequenceDiagram
	autonumber
	
	actor User
	participant API
	participant Token
	participant Seat
	participant Reservation
	participant Database
	
	User ->> +API: 좌석 예약 요청
	API ->> +Token: 토큰 유효성 검증
	Token ->> +Database: 토큰 조회
	Database -->> -Token: 토큰 반환
	Token -->> -API: 토큰 검증 결과
	alt 유효한 토큰
	API ->> +Seat: 좌석 예약 상태 요청
	Seat ->> +Database: 좌석 조회
	Database -->> -Seat: 좌석 반환
	Seat -->> -API: 좌석 예약 상태 반환
	alt 좌석 예약 가능 
	API ->> +Reservation: 좌석 예약 요청
	Reservation ->> +Database: 예약 정보 저장
	Database --x -Reservation: 예약 정보 저장 완료
	Reservation -->> -API: 좌석 점유 성공 반환
	API ->> +Seat: 좌석 상태 변경 요청
	Seat ->> +Database: 좌석 상태 변경
	Database --x -Seat: 좌석 상태 변경 완료
	Seat -->> -API: 좌석 반환
	API -->> -User: 임시 배정 좌석 반환
	else 좌석 예약 불가
	API -->> User: 오류 메시지 반환
	end
	else 유효하지 않은 토큰
	API -->> User: 오류 메시지 반환
	end
```

## 결제 요청
``` mermaid
sequenceDiagram
	autonumber
	
	actor User
	participant API
	participant Token
	participant Payment
	participant Seat
	participant Reservation
	participant Database
	
	User ->> +API: 결제 요청
	API ->> +Token: 토큰 유효성 검증
	Token ->> +Database: 토큰 조회
	Database -->> -Token: 토큰 반환
	Token -->> -API: 토큰 검증 결과
	alt 유효한 토큰
	API ->> +Payment: 결제 요청
	Payment ->> +Database: 잔액 조회
	Database -->> -Payment: 잔액 반환
	alt 잔액 충족
	Payment ->> +Database: 결제 처리
	Database --x -Payment: 처리 완료
	Payment -->> -API: 결제 내역 반환
	API ->> +Seat: 좌석 상태 변경 요청
	Seat ->> +Database: 좌석 상태 변경
	Database --x -Seat: 좌석 상태 변경 완료
	Seat -->> -API: 좌석 상태 변경 완료
	API ->> +Reservation: 예약 성공 요청
	Reservation ->> +Database: 예약 정보 상태 변경 및 조회
	Database --x -Reservation: 예약 정보 반환
	Reservation -->> -API: 예약 정보 반환
	API ->> +Token: 토큰 만료 요청
	Token ->> +Database: 토큰 만료 변경
	Database --x -Token: 토큰 만료 변경 완료
	Token -->> -API: 토큰 만료 성공
	API -->> -User: 예약 및 결제 정보 반환
	else 잔액 부족
	Payment -->> User: 오류 메시지 반환
	end
	else 유효하지 않은 토큰
	API -->> User: 오류 메시지 반환
	end
```
