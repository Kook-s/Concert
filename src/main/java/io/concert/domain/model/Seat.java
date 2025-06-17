package io.concert.domain.model;

import io.concert.infra.enums.SeatStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Seat(
        long id,
        String seatNo,
        SeatStatus status,
        int seatPrice,
        long scheduleId,
        Long reservationId
) {

    public Seat withReservation(long reservationId) {
        return Seat.builder()
                .id(id)
                .seatNo(seatNo)
                .status(SeatStatus.RESERVED)
                .seatPrice(seatPrice)
                .scheduleId(scheduleId)
                .reservationId(reservationId)
                .build();
    }

}
