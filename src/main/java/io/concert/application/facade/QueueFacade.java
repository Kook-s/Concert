package io.concert.application.facade;

import io.concert.domain.model.Queue;
import io.concert.domain.service.QueueService;
import io.concert.domain.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class QueueFacade {

    private final UserService userService;
    private final QueueService queueService;

    public Queue issueToken(String lockName, Long userId) {
        userService.validateUser(userId);
        // 토큰 발급
        return queueService.issueToken(userId);
    }

    @Cacheable(value = "queueStatus", key = "#token", cacheManager = "caffeineCacheManager")
    public Queue status(String token, Long userId) {
        userService.validateUser(userId);

        return queueService.getToken(token);
    }
}
