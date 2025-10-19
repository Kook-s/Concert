package io.concert.application.facade;

import io.concert.application.dto.ReservationCommand;
import io.concert.application.dto.ReservationResult;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ReservationRepository;
import io.concert.domain.service.ConcertService;
import io.concert.domain.service.ReservationService;
import io.concert.support.aop.DistributedLock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final ConcertService concertService;
    private final ReservationService reservationService;

    @DistributedLock(key = "#lockName")
    @Transactional
    public ReservationResult reservation(String lockName, ReservationCommand command) {
        ConcertSchedule schedule = concertService.getSchedule(command.scheduleId());
        Seat seat = concertService.getSeat(command.seatId());
        concertService.validateReservationAvailability(schedule, seat);

        concertService.assignmentSeat(seat);

        Reservation reservation = reservationService.reservation(schedule, seat, command.userId());

        return ReservationResult.from(reservation, schedule, seat);
    }
}
