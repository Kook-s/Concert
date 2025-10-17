package io.concert.application.scheduler;

import io.concert.domain.component.port.MessageProducer;
import io.concert.domain.model.OutboxEvent;
import io.concert.domain.model.PaymentEvent;
import io.concert.domain.repository.OutboxRepository;
import io.concert.support.type.OutboxStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxRetryScheduler {

    private final OutboxRepository outboxRepository;
    private final MessageProducer producer;

    @Scheduled(fixedDelay = 10000)
    public void retryFailOutboxEvents() {
        List<OutboxEvent> failedEvents = outboxRepository.findByStatusNot(OutboxStatus.SEND_SUCCESS.name());

        failedEvents.forEach(event -> {
            if (event.getStatus().equals(OutboxStatus.INIT.name()) && event.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(1)))
                return;
            producer.send(PaymentEvent.mapToPaymentEvent(event));
        });
    }

}
