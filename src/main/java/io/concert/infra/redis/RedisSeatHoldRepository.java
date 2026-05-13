package io.concert.infra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class RedisSeatHoldRepository implements SeatHoldRepository{

    private final StringRedisTemplate redisTemplate;

    private static final String HOLD_KEY = "seat::hold";

    @Override
    public void save(Long seatId, Duration ttl) {
        String key = HOLD_KEY + ":" + seatId;
        redisTemplate.opsForValue().set(key, seatId.toString(), ttl);
    }

    @Override
    public void release(Long seatId) {
        String key = HOLD_KEY + ":" + seatId;
        redisTemplate.delete(key);
    }
}
