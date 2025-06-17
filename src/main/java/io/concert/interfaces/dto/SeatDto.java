package io.concert.interfaces.dto;

import io.concert.infra.enums.SeatStatus;
import lombok.Builder;

@Builder
public record SeatDto(
        long id,
        String seatNo,
        SeatStatus status,
        int seatPrice,
        long scheduleId,
        long reservationId
) {
}
