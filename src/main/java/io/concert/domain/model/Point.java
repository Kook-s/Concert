package io.concert.domain.model;

import io.concert.infra.entity.UserEntity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

import java.time.LocalDateTime;

public record Point(
        long id,
        long userId,
        int amount,
        LocalDateTime updatedAt
) {
}
