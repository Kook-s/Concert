package io.concert.interfaces.interceptor.controller;

import io.concert.application.dto.SeatResult;
import io.concert.application.facade.ConcertFacade;
import io.concert.domain.model.Concert;
import io.concert.domain.model.ConcertSchedule;
import io.concert.interfaces.dto.GetConcertDto;
import io.concert.interfaces.dto.GetScheduleDto;
import io.concert.interfaces.dto.GetSeatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/concerts")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    /**
     * 콘서트 목록 조회
     */
    @GetMapping
    public ResponseEntity<GetConcertDto.ConcertResponse> getConcerts() {
        List<Concert> concerts = concertFacade.getConcerts();
        return ResponseEntity.ok(GetConcertDto.ConcertResponse.of(concerts));
    }

    /**
     * 예약 가능한 일정 조회
     */
    @GetMapping("/{concertId}/schedules")
    public ResponseEntity<GetScheduleDto.ScheduleResponse> getConcertSchedules(
            @PathVariable Long concertId
    ) {
        List<ConcertSchedule> schedules = concertFacade.getConcertSchedules(concertId);
        return ResponseEntity.ok(GetScheduleDto.ScheduleResponse.of(concertId, schedules));
    }

    /**
     * 예약 가능한 좌석 조회
     */
    @GetMapping("/{concertId}/schedules/{scheduleId}/seats")
    public ResponseEntity<GetSeatDto.SeatResponse> getSeat(
            @PathVariable Long concertId,
            @PathVariable Long scheduleId
    ) {
        SeatResult seats = concertFacade.getSeats(concertId, scheduleId);
        return ResponseEntity.ok(GetSeatDto.SeatResponse.of(seats));

    }
}
