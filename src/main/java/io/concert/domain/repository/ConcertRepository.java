package io.concert.domain.repository;

import io.concert.domain.model.Concert;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Seat;
import io.concert.support.type.SeatStatus;

import java.util.List;

public interface ConcertRepository {

    List<Concert> findConcerts();

    List<ConcertSchedule> findConcertSchedules(Long concertId);

    Concert findConcert(Long concertId);

    List<Seat> findSeats(Long concertId, Long scheduleId, SeatStatus seatStatus);

    ConcertSchedule findConcertSchedule(Long scheduleId);

    void saveSeat(Seat seat);

    Seat findSeat(Long seatId);


}
