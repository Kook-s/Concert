package io.concert.domain.repository;

import io.concert.domain.model.Reservation;
import io.concert.support.type.ReservationStatus;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository {
    Reservation save(Reservation reservation);

    Reservation findById(String reservationId);

    List<Reservation> findByConcertIdAndScheduleAndSeatId(Long concertId, Long scheduleId, Long seatId);

    List<Reservation> findExpiredReservation(ReservationStatus reservationStatus, LocalDateTime localDateTime);
}
