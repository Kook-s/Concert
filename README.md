# 콘서트 대기열

## Introduction
대규모 트래픽 환경에서도 안정적으로 동작하는 대기열 시스템을 직접 경험해보고자 시작한 사이드 프로젝트입니다.
`Spring Boot`, `MySQL`, `Redis`, `Kafka를` 기반으로 실제 서비스와 유사한 구조를 구현했습니다.

대기열 생성, 순번 관리, 보호된 API 접근, 결제 시간 초과 시 자동 취소 등 실제 콘서트 예약 시스템에서 발생하는 핵심 기능들을 직접 설계하고 구현했으며, 이를 통해 동시성 제어, 분산락, 메시지 처리 전략까지 다뤄볼 수 있도록 구성했습니다.

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
- 사용자 대기열 토큰 발급
- 사용자별 대기 상태 조회 (순번/잔여 시간)
- 대기열 통과 후 보호된 API접근 제어
- Redis TTL 결제 제한 시간 관리
- 결제 시간 초과 시 자동 취소 처리
- Redisson 기반 분산락 적용

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