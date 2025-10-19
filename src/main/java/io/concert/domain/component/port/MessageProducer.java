package io.concert.domain.component.port;

import io.concert.domain.model.PaymentEvent;

public interface MessageProducer {

    void send(PaymentEvent event);
}
