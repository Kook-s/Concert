package io.concert.infra.enums;

public enum ConcertStatus {
    READY,     // 콘서트 등록 완료, 스케줄만 있음 (예약 시작 전)
    OPEN,      // 예약 진행 중
    CLOSED,    // 예약 마감됨 (공연 전)
    COMPLETED, // 공연 완료
    CANCELLED  // 공연 취소
}