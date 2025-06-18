package io.concert.interfaces.dto;

import lombok.Builder;

@Builder
public record ReservationCommand(
        long userId,
        long scheduleId,
        long seatId
) {
}
