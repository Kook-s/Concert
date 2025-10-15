package io.concert.domain.service;

import io.concert.domain.dto.PaymentEventCommand;
import io.concert.domain.model.PaymentEvent;
import io.concert.support.type.OutboxStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentEventService {

    @Value("${event.payment.topic}")
    private String topic;

    private final ApplicationEventPublisher eventPublisher;

    public void publicEvent(PaymentEventCommand command) {
        try {
            eventPublisher.publishEvent(PaymentEvent.from(topic, OutboxStatus.INIT, command));
        } catch (Exception e) {
            log.error("Failed to publish payment event: ", e);
        }
    }
}
