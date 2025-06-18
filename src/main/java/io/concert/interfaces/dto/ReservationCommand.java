package io.concert.interfaces.dto;

public record ReservationDto(
        long userId,
        long scheduleId,
        long seatId
) {



}
