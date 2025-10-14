package io.concert.infra.entity;

import io.concert.domain.model.OutboxEvent;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String topic;

    @Column(nullable = false)
    private String eventKey;

    @Lob
    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime processedAt;

    @Column(nullable = false)
    private String uuid;

    public OutboxEvent of() {
        return OutboxEvent.builder()
                .id(id)
                .topic(topic)
                .key(eventKey)
                .payload(payload)
                .type(type)
                .status(status)
                .createAt(createdAt)
                .uuid(uuid)
                .build();
    }

    public static OutboxEvent from(OutboxEvent event) {
        return OutboxEvent.builder()
                .id(event.id())
                .topic(event.topic())
                .key(event.key())
                .payload(event.payload())
                .type(event.type())
                .status(event.status())
                .createAt(event.createAt())
                .uuid(event.uuid())
                .build();
    }
}
