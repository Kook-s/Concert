package io.concert.application;

import io.concert.domain.model.Reservation;
import io.concert.domain.model.Schedule;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ReservationRepository;
import io.concert.domain.service.ConcertService;
import io.concert.domain.service.ReservationService;
import io.concert.domain.service.ScheduleService;
import io.concert.domain.service.SeatService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReservationFacade {

    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;
    private final ReservationService reservationService;

    public Reservation getUserReservation(long userId){
        return reservationService.getUserReservation(userId);
    }

    @Transactional
    public Reservation reservation(long scheduleId, long seatId, long userId){
        Schedule schedule = scheduleService.getSchedule(scheduleId);
        Seat seat = seatService.getSeat(seatId);

        // 예약
        Reservation resultReservation = reservationService.reservation(userId, schedule);
        Seat result = seatService.reservationSeat(seat, schedule.id());

        return resultReservation;
    }

}
