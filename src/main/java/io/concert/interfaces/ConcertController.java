package io.concert.interfaces;

import io.concert.application.ConcertFacade;
import io.concert.domain.model.Concert;
import io.concert.domain.model.Schedule;
import io.concert.domain.model.Seat;
import io.concert.interfaces.dto.GetConcertDto;
import io.concert.interfaces.dto.GetScheduleDto;
import io.concert.interfaces.dto.GetSeatResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    // 콘서트 목록 조회
    @GetMapping
    public ResponseEntity<GetConcertDto.ConcertResponse> getConcert() {
        List<Concert> concerts = concertFacade.getConcerts();

        return ResponseEntity.ok(GetConcertDto.ConcertResponse.of(concerts));
    }

    @GetMapping("/{concertId}/schedule")
    public ResponseEntity<GetScheduleDto.ScheduleResponse> getConcertSchedule(@PathVariable Long concertId) {
        List<Schedule> concertSchedule = concertFacade.getConcertSchedule(concertId);

        return ResponseEntity.ok(GetScheduleDto.ScheduleResponse.of(concertSchedule));
    }

    @GetMapping("/{scheduleId}/seat")
    public ResponseEntity<GetSeatResponse.SeatResponse> getConcertSeat(@PathVariable Long scheduleId) {
        List<Seat> seats = concertFacade.getSeats(scheduleId);

        return ResponseEntity.ok(GetSeatResponse.SeatResponse.of(seats));
    }
}


















