package io.concert.application;

import io.concert.domain.model.Concert;
import io.concert.domain.model.Schedule;
import io.concert.domain.model.Seat;
import io.concert.domain.service.ConcertService;
import io.concert.domain.service.ScheduleService;
import io.concert.domain.service.SeatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;
    private final ScheduleService scheduleService;
    private final SeatService seatService;


    // 콘서트 전체 조회
    public List<Concert> getConcerts(){
        return concertService.getConcertLit();
    }

    // 콘서트 날짜 조회
    public List<Schedule> getConcertSchedule(long concertId) {
        Concert concert = concertService.getConcert(concertId);
        return scheduleService.getConcertSchedules(concert.id());
    }

    // 해당(특정 날짜) 콘서트의 예약 가능한 좌석 조회
    public List<Seat> getSeats(long scheduleId) {
        Schedule schedule = scheduleService.getSchedule(scheduleId);
        return seatService.getAvailableSeats(schedule.id());
    }
}
