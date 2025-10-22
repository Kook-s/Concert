package io.concert.application.scheduler;

import io.concert.domain.service.QueueService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenScheduler {

    private final QueueService queueService;

    @Scheduled(fixedDelay = 10000)
    public void adjustActiveToken() {
        queueService.updateActiveTokens();
    }
}
