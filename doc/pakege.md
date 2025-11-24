# 패키지 구조
프로젝트는 DDD(Domain-Driven Design)를 기반으로 한 Clean Architecture 스타일의 계층형 구조로 설계했습니다.
도메인을 중심에 두고, 애플리케이션 레이어에서 유스케이스를 조립하며, 인터페이스와 인프라 레이어가 외부와의 입출력 및 구현체를 담당하는 형태입니다.
비즈니스 규칙과 기술 구현을 분리해 유지보수성과 확장성을 높인 구조입니다.
```bash
├── application
│   ├── dto
│   ├── facade
│   └── scheduler
├── domain
│   ├── component
│   ├── dto
│   ├── model
│   ├── repository
│   └── service
├── infra
│   ├── config
│   ├── entity
│   ├── kafka
│   └── repository
├── interfaces
│   ├── dto
│   ├── exception
│   ├── interceptor
│   └── kafka
└── support
    ├── aop
    ├── code
    ├── exception
    └── type
```