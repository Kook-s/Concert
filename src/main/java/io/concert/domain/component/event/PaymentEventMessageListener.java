package io.concert.domain.component.event;

import io.concert.domain.component.port.MessageProducer;
import io.concert.domain.model.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventMessageListener {

    private final MessageProducer messageProducer;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void sendMessageHandler(PaymentEvent event) {
        messageProducer.send(event);
        log.info("Sent event to Kafka topic={}, key={}, payload={}", event.getTopic(), event.getKey(), event.getPayload());
    }
}
