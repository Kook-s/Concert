package io.concert.domain.model;

import io.concert.support.type.SeatStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Seat(
        Long id,
        Long concertScheduleId,
        int seatNo,
        SeatStatus status,
        LocalDateTime reservationAt,
        int seatPrice
) {
}
