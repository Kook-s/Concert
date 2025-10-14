package io.concert.application.dto;

public record ReservationCommand(
        Long userId,
        Long concertId,
        Long scheduleId,
        Long seatId
) {
}
