package io.concert.infra.enums;

public enum QueueStatus {
    WAITING,    // 대기 중
    COMPLETED,  // 처리 완료
    CANCELLED,  // 취소 상태
    EXPIRED     // 만료
}
