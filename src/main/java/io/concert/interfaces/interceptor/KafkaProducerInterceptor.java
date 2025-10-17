package io.concert.interfaces.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
public class KafkaProducerInterceptor implements ProducerInterceptor<String, String> {

    @Override
    public ProducerRecord<String, String> onSend(ProducerRecord<String, String> record) {
        log.info("[Kafka] message body: {}", record.value());
        log.info("[Kafka] message headers: {}", record.headers());
        return record;
    }

    @Override
    public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
        if (exception == null) {
            log.info("[Kafka] Message sent successfully to topic={}, partition={}", metadata.topic(), metadata.partition());
        } else {
            log.error("[Kafka] Message send failed", exception);
        }
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
