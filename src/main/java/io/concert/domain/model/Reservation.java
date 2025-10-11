package io.concert.domain.model;

import io.concert.support.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Reservation(
        Long id,
        Long concertId,
        Long scheduleId,
        Long seatId,
        Long userId,
        ReservationStatus status,
        LocalDateTime reservationAt
) {
}
