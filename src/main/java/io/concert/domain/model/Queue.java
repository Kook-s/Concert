package io.concert.domain.model;

import io.concert.infra.entity.UserEntity;
import io.concert.infra.enums.QueueStatus;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record Queue(
        long id,
        long userId,
        String token,
        QueueStatus status,
        LocalDateTime expiredAt,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
