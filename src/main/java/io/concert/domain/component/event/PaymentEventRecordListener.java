package io.concert.domain.component.event;

import io.concert.domain.model.PaymentEvent;
import io.concert.domain.repository.OutboxRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentEventRecordListener {

    private final OutboxRepository outboxRepository;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void paymentInitHandler(PaymentEvent event) {
        log.info("paymentInitHandler={}", event);
        outboxRepository.save(event);
    }

    @EventListener(condition = "#event.status != 'INIT'")
    public void paymentSuccessHandler(PaymentEvent event) {
        String uuid = event.getUuid();
        PaymentEvent paymentEvent = PaymentEvent.mapToPaymentEvent(outboxRepository.findByUuid(uuid));
        paymentEvent.setStatus(event.getStatus());
        outboxRepository.save(paymentEvent);
    }
}
