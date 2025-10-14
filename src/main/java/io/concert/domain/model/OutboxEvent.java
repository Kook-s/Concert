package io.concert.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record OutboxEvent(
        Long id,
        String topic,
        String key,
        String payload,
        String type,
        String status,
        LocalDateTime createAt,
        String uuid
) {
}
