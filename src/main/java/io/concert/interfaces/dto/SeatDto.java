package io.concert.interfaces.dto;

import io.concert.support.type.SeatStatus;
import lombok.Builder;

@Builder
public record SeatDto(
        Long seatId,
        int seatNo,
        SeatStatus seatStatus,
        int seatPrice
) {
}
