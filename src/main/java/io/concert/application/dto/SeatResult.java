package io.concert.application.dto;

import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Seat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record SeatResult(
        Long scheduleId,
        Long concertId,
        LocalDateTime concertAt,
        List<Seat> seats
) {

    public static SeatResult from(ConcertSchedule schedule, List<Seat> seat) {
        return SeatResult.builder()
                .scheduleId(schedule.id())
                .concertId(schedule.concertId())
                .concertAt(schedule.concertAt())
                .seats(seat)
                .build();
    }
}
