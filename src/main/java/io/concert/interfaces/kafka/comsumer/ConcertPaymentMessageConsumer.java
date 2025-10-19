package io.concert.interfaces.kafka.comsumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class ConcertPaymentMessageConsumer {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "concert-payment", groupId = "concert-group")
    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        try {
            String payload = record.value();
            String paymentType = new String(
                    record.headers().lastHeader("payment-type").value(),
                    StandardCharsets.UTF_8
            );

            log.info("Received message: type={}, payload={}", paymentType, payload);

            if("COMPLETED".equals(paymentType)) {
                // ture
            } else if("CANCELLED".equals(paymentType)) {
                // false
            }
            acknowledgment.acknowledge();
        } catch (Exception e) {
            log.error("Error while processing Kafka message", e);
        }
    }

}
