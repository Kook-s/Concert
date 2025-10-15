package io.concert.application.dto;

import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.service.ConcertService;
import io.concert.support.type.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReservationResult(
        Long reservationId,
        Long concertId,
        LocalDateTime concertAt,
        Seat seat,
        ReservationStatus status
) {

    public static ReservationResult from(Reservation reservation, ConcertSchedule schedule, Seat seat) {
        return ReservationResult.builder()
                .reservationId(reservation.id())
                .concertId(schedule.concertId())
                .concertAt(schedule.concertAt())
                .seat(Seat.builder().id(seat.id()).build())
                .status(reservation.status())
                .build();
    }

}
