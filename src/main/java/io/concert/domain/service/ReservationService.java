package io.concert.domain.service;

import io.concert.domain.model.Reservation;
import io.concert.domain.model.Schedule;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ReservationRepository;
import io.concert.infra.repository.jpa.ReservationJpaRepository;
import io.concert.support.CustomException;
import io.concert.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation getReservation(long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public Reservation getUserReservation(long id) {
        return reservationRepository.findByUserId(id)
                .orElseThrow(() -> new CustomException(ErrorCode.RESERVATION_NOT_FOUND));
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation reservation(long userId, Schedule schedule) {
        Reservation reservation = Reservation.create(userId, schedule.id());
        return reservationRepository.reservation(reservation);
    }

}
