package io.concert.infra.kafka.producer;

import io.concert.domain.component.port.MessageProducer;
import io.concert.domain.model.PaymentEvent;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaMessageProducer implements MessageProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Override
    public void send(PaymentEvent event) {
        try {
            ProducerRecord<String, String> producerRecord = new ProducerRecord<>(event.getTopic(), event.getKey(), event.getPayload());
            producerRecord.headers().add("payment-type", event.getType().getBytes(StandardCharsets.UTF_8));
            kafkaTemplate.send(producerRecord);
        } catch (Exception e) {
            log.error("Error publishing event to Kafka", e);
            throw new CoreException(ErrorType.INTERNAL_ERROR, "Error publishing event to Kafka");
        }
    }
}
