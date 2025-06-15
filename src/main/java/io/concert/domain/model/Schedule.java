package io.concert.domain.model;

import java.time.LocalDateTime;

public record Schedule(
        long id,
        long concertId,
        LocalDateTime concertAt,
        LocalDateTime reservationAt,
        LocalDateTime deadlineAt
) {
}
