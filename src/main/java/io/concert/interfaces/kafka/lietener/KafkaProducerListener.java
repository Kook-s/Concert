package io.concert.interfaces.kafka.lietener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaProducerListener implements ProducerListener<String, String> {

//    private final ApplicationEventPublisher eventPublisher;
//    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    @Override
    public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
        log.info("[Kafka] Message sent successfully topic={}, partition={}, offset={}",
                recordMetadata.topic(), recordMetadata.partition(), recordMetadata.offset());
    }

    @Override
    public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
        log.error("[Kafka] Message send failed topic={}",
                recordMetadata.topic(), exception);
    }
}
