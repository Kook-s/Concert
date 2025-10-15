package io.concert.application.facade;

import io.concert.application.dto.SeatResult;
import io.concert.domain.model.Concert;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Seat;
import io.concert.domain.service.ConcertService;
import io.concert.support.type.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ConcertFacade {

    private final ConcertService concertService;

    @Cacheable(value = "concert", key = "'all'", cacheManager = "redisCacheManager")
    public List<Concert> getConcerts() {
        return concertService.getConcerts();
    }

    @Cacheable(value = "schedule", key = "#concertId", cacheManager = "redisCacheManager")
    public List<ConcertSchedule> getConcertSchedules(Long concertId) {
        Concert concert = concertService.getConcert(concertId);
        return concertService.getConcertSchedules(concert);
    }

    @Cacheable(value = "shortLiveCache", key = "#scheduleId", cacheManager = "redisCacheManager")
    public SeatResult getSeats(Long concertId, Long scheduleId) {
        Concert concert = concertService.getConcert(concertId);
        ConcertSchedule schedule = concertService.getSchedule(scheduleId);
        List<Seat> seats = concertService.getSeats(concert.id(), schedule.id(), SeatStatus.AVAILABLE);

        return SeatResult.from(schedule, seats);
    }
}
