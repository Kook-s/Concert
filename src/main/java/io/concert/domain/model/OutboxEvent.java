package io.concert.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class OutboxEvent {
    private Long id;
    private String topic;
    private String key;
    private String payload;
    private String type; // 이벤트 타입 (PAYMENT_COMPLETED)
    private String status; // 이벤트 상태 (INIT, SUCCESS)
    private LocalDateTime createdAt;
    private String uuid;
}
