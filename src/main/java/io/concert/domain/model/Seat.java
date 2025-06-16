package io.concert.domain.model;

import io.concert.infra.enums.SeatStatus;

import java.time.LocalDateTime;

public record Seat(
        long id,
        String seatNo,
        SeatStatus status,
        int seatPrice,
        long scheduleId,
        long reservationId
) {
}
