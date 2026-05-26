package io.concert.infra.redis;

import java.time.Duration;

public interface SeatHoldRepository {
    //좌석 점유
    void save(Long seatId, Duration ttl);

    //결제 완료시 ttl 제거
    void release(Long seatId);
}
