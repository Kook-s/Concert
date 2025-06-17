package io.concert.domain.model;

import io.concert.infra.enums.ReservationStatus;

import java.time.LocalDateTime;

public record Reservation(
        long id,
        long userId,
        long scheduleId,
        ReservationStatus status,
        LocalDateTime reservationAt
) {
}
