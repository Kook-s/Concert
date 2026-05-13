package io.concert.domain.service;

import io.concert.domain.model.Concert;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ConcertRepository;
import io.concert.infra.redis.RedisSeatHoldRepository;
import io.concert.support.type.SeatStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;
    private final RedisSeatHoldRepository redisSeatHoldRepository;

    public List<Concert> getConcerts() {
        return concertRepository.findConcerts();
    }

    public Concert getConcert(Long concertId) {
        return concertRepository.findConcert(concertId);
    }

    public List<ConcertSchedule> getConcertSchedules(Concert concert) {
        if (!concert.checkStatus()) {
            return null;
        }

        return concertRepository.findConcertSchedules(concert.id());
    }

    public List<Seat> getSeats(Long concertId, Long scheduleId, SeatStatus status) {
        return concertRepository.findSeats(concertId, scheduleId, status);
    }

    public ConcertSchedule getSchedule(Long scheduleId) {
        return concertRepository.findConcertSchedule(scheduleId);
    }

    public void validateReservationAvailability(ConcertSchedule schedule, Seat seat) {
        schedule.checkStatus();
        seat.checkStatus();
    }

    public void assignmentSeat(Seat seat) {
        Seat assignment = seat.assign();
        concertRepository.saveSeat(assignment);

        redisSeatHoldRepository.save(seat.id(), Duration.ofMinutes(5));
    }

    public void releaseSeatHold(Long seatId) {
        redisSeatHoldRepository.release(seatId);
    }

    public Seat getSeat(Long seatId) {
        return concertRepository.findSeat(seatId);
    }


}
