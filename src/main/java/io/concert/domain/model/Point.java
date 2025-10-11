package io.concert.domain.model;

import java.time.LocalDateTime;

public record Point(
        Long id,
        Long userId,
        Long amount,
        LocalDateTime lastUpdatedAt
) {
}
