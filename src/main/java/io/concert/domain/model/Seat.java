package io.concert.domain.model;

import io.concert.support.type.SeatStatus;

import java.time.LocalDateTime;

public record Seat(
        Long id,
        Long concertScheduleId,
        int seatNo,
        SeatStatus status,
        LocalDateTime reservationAt,
        int seatPrice
) {
}
