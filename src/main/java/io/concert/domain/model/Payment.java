package io.concert.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Payment(
        Long id,
        Long reservationId,
        Long userId,
        int amount,
        LocalDateTime paymentAt
) {
}
