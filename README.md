# 콘서트 대기열

## Introduction
콘서트 예매 트래픽 상황을 가정해 대기열 제어, 좌석 예약 동시성 처리, 결제 이벤트 흐름을 구현한 프로젝트입니다.
대량 사용자 요청이 동시에 유입되는 상황에서도 예약 진입 순서를 제어하고, 동일 좌석 중복 예약을 방지하며, 결제 완료 이후의 후속 처리를 비동기로 연결하는 구조를 목표로 설계했습니다.

`Spring Boot`, `MySQL`, `Redis`, `Redisson`, `Kafka`를 기반으로 대기열 생성, 순번 관리, 보호된 API 접근 제어, 좌석 예약 직렬화, 결제 시간 초과 시 자동 취소 등 실제 예매 시스템에서 발생하는 핵심 흐름을 다뤘습니다.

## Project Info
- 기간: 2024.10 ~ 2024.12
- GitHub: https://github.com/kook-s/Concert

## Document
### [시퀀스다이어그램](doc/sequence.md)
### [ERD](doc/erd.md)
### [API명세서 YML](doc/swagger.yaml)
- [API명세서 설명](doc/swagger-guide.md)
### [Architecture Overview](doc/pakege.md)

## Tech Stack
- Java 17
- Spring Boot 3.3.4
- Database: MySQL
- ORM: JPA
- Cache/Lock: Redis, Redisson, Caffeine Cache
- Messaging: Kafka

## Features
- Redis Sorted Set과 TTL 기반 대기열 토큰 발급 및 활성/대기 상태 관리
- 사용자별 대기 상태 조회와 순번/예상 잔여 시간 제공
- Interceptor 기반 토큰 검증으로 대기열을 통과하지 않은 사용자의 예약 API 직접 접근 차단
- Redisson 분산 락을 활용한 동일 좌석 예약 요청 직렬화 및 중복 예약 방지
- Kafka 기반 결제 완료 이벤트 비동기 처리와 Outbox 재시도 로직
- 예약 만료 스케줄러를 통한 결제 미완료 좌석 자동 복구 처리
- Redis TTL 기반 결제 제한 시간 관리

## Performance
- 대기열 토큰 발급 API 300건 처리 시 평균 응답 시간 26.89ms, p95 44.81ms, 실패율 0% 기록
- 좌석 조회 API를 30 VU, 20초 조건으로 검증해 총 228,022건 처리, 평균 응답 시간 2.60ms, p95 3.38ms, 실패율 0% 기록
- 동일 좌석 동시 예약 10건 테스트에서 1건만 예약 성공, DB 검증 결과 중복 예약 0건 확인

## Project Structure
프로젝트는 DDD(Domain-Driven Design)를 기반으로 한 Clean Architecture 스타일의 계층형 구조로 설계했습니다.
도메인을 중심에 두고, 애플리케이션 레이어에서 유스케이스를 조립하며, 인터페이스와 인프라 레이어가 외부와의 입출력 및 구현체를 담당하는 형태입니다.
비즈니스 규칙과 기술 구현을 분리해 유지보수성과 확장성을 높인 구조입니다.
```bash
├── ConcertApplication.java
├── application
├── domain
├── infra
├── interfaces
└── support
 ```

## How to Run
### 1. Run MySQL / Redis / Kafka
```bash
docker compose up -d
```
### 2. Run Spring Boot
```bash
./gradlew bootRun
```
