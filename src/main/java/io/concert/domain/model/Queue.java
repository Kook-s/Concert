package io.concert.domain.model;

import io.concert.infra.entity.UserEntity;
import io.concert.infra.enums.QueueStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

public record Queue(
        long id,
        long userId,
        String token,
        QueueStatus status,
        LocalDateTime expiredAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    public static Queue createNew(long userId) {
        return new Queue(
            0L,
            userId,
            UUID.randomUUID().toString(),
            QueueStatus.WAITING,
            null,
            LocalDateTime.now(),
            null
        );
    }
}
