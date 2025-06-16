package io.concert.domain.model;

import io.concert.infra.enums.ConcertStatus;

import java.time.LocalDateTime;

public record Schedule(
        long id,
        long concertId,
        ConcertStatus status,
        LocalDateTime concertAt,
        LocalDateTime reservationAt,
        LocalDateTime deadlineAt
) {
}
