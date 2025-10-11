package io.concert.domain.service;

import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ReservationRepository;
import io.concert.support.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public Reservation reservation(ConcertSchedule schedule, Seat seat, Long userId) {
        //예약 정보 생성
        Reservation reservation = Reservation.create(schedule, seat.id(), userId);

        //예약 정보 저장
        return reservationRepository.save(reservation);
    }

    public Reservation checkReservation(Long reservationId, Long userId) {
        Reservation findReservation = reservationRepository.findById(reservationId);
        findReservation.checkValidation(userId);
        return findReservation;
    }

    public Reservation changeStatus(Reservation reservation, ReservationStatus status) {
         Reservation changeReservation = reservation.changeStatus(status);
         return reservationRepository.save(changeReservation);
    }

}
