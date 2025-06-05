package io.concert.infra.enums;

public enum ReservationStatus {
    PENDING,    // 예약 대기 중
    COMPLETED,  // 예약 확정
    CANCELLED,  // 사용자에 의해 예약 취소
    EXPIRED     // 시간 초과 등으로 예약 만료
}
