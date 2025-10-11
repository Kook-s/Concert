package io.concert.domain.model;

import java.time.LocalDateTime;

public record Payment(
        Long id,
        Long reservationId,
        Long userId,
        int amount,
        LocalDateTime paymentAt
) {
}
