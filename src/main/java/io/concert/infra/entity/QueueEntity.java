package io.concert.infra.entity;

import io.concert.domain.model.Queue;
import io.concert.infra.enums.QueueStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "queue")
@NoArgsConstructor
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

    private LocalDateTime expiredAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


    public Queue toQueue() {
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

}
