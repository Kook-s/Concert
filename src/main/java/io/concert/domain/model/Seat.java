package io.concert.domain.model;

import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
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

    public void checkStatus() {
        if (status.equals(SeatStatus.UNAVAILABLE)) {
            throw new CoreException(ErrorType.SEAT_UNAVAILABLE, "좌석 상태: " + status);
        }
    }

    public Seat assign() {
        return Seat.builder()
                .id(id)
                .concertScheduleId(concertScheduleId)
                .seatNo(seatNo)
                .status(SeatStatus.UNAVAILABLE)
                .reservationAt(LocalDateTime.now())
                .seatPrice(seatPrice)
                .build();
    }

    public Seat toAvailable() {
        if (this.status == SeatStatus.UNAVAILABLE) {
            return Seat.builder()
                    .id(id)
                    .concertScheduleId(concertScheduleId)
                    .seatNo(seatNo)
                    .status(SeatStatus.AVAILABLE)
                    .reservationAt(LocalDateTime.now())
                    .seatPrice(seatPrice)
                    .build();
        }
        return null;
    }
}
