package io.concert.application.dto;

import lombok.Builder;

@Builder
public record ReservationCommand(
        Long userId,
        Long concertId,
        Long scheduleId,
        Long seatId
) {
}
