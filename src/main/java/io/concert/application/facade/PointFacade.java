package io.concert.application.facade;

import io.concert.domain.model.Point;
import io.concert.domain.service.PointService;
import io.concert.domain.service.UserService;
import io.concert.support.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {

    private final UserService userService;
    private final PointService pointService;

    @Cacheable(value = "point", key = "#userId", cacheManager = "caffeineCacheManager")
    public Point getPoint(Long userId) {
        userService.validateUser(userId);
        return pointService.getPoint(userId);
    }

    @DistributedLock(key = "#lockName")
    public Point chargePoint(String lockName, Long userId, Long amount) {
        userService.validateUser(userId);
        return pointService.chargePoint(userId, amount);
    }



}
