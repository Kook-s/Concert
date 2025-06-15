package io.concert.infra.entity;

import io.concert.domain.model.Queue;
import io.concert.infra.enums.QueueStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "queue")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class QueueEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "queue_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    private String token;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QueueStatus status;

    private LocalDateTime expiredAt; // 만료 시각
    private LocalDateTime createdAt; // 생성 시각
    private LocalDateTime updatedAt; // 대기열 정보 변경 시각(입장)

    public Queue toDomain() {
        return new Queue(
                id,
                user.getId(),
                token,
                status,
                expiredAt,
                createdAt,
                updatedAt
        );
    }

    public static QueueEntity from(Queue queue) {
        return QueueEntity.builder()
                .id(queue.id())
                .user(UserEntity.builder().id(queue.userId()).build())
                .token(queue.token())
                .status(queue.status())
                .expiredAt(queue.expiredAt())
                .createdAt(queue.createdAt())
                .updatedAt(queue.updatedAt())
                .build();

    }

}

