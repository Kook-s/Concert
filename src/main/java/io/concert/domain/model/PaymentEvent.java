package io.concert.domain.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.concert.domain.dto.PaymentEventCommand;
import io.concert.support.type.OutboxStatus;
import io.concert.support.type.PaymentStatus;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PaymentEvent extends OutboxEvent {

    public PaymentEvent(Long id, String topic, String key, String payload, String type, String status, LocalDateTime createdAt, String uuid) {
        super(id, topic, key, payload, type, status, createdAt, uuid);
    }

    public static PaymentEvent from(String topic, OutboxStatus outboxStatus, PaymentEventCommand command) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String payload = objectMapper.writeValueAsString(command);

        return new PaymentEvent(null, topic, String.valueOf(command.userId()), payload, PaymentStatus.COMPLETED.name(), outboxStatus.name(), LocalDateTime.now(), command.uuid());
    }
    public static PaymentEvent mapToPaymentEvent(OutboxEvent outboxEvent) {
        // OutboxEvent의 필드를 이용해서 PaymentEvent 생성
        return new PaymentEvent(
                outboxEvent.getId(),
                outboxEvent.getTopic(),
                outboxEvent.getKey(),
                outboxEvent.getPayload(),
                outboxEvent.getType(),
                outboxEvent.getStatus(),
                outboxEvent.getCreatedAt(),
                outboxEvent.getUuid()
        );
    }
}
