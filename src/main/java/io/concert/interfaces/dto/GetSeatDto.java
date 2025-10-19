package io.concert.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.concert.application.dto.SeatResult;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetSeatDto {

    private final static Long MAX_SEAT = 50L;

    @Builder
    public record SeatResponse(
            Long concertId,
            Long scheduleId,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime concertAt,
            Long maxSeats,
            List<SeatDto> seats
    ) {

        public static SeatResponse of(SeatResult seats) {
            List<SeatDto> list = (seats.seats() != null) ? seats.seats().stream()
                    .map(seat ->
                            SeatDto.builder()
                                    .seatId(seat.id())
                                    .seatNo(seat.seatNo())
                                    .seatStatus(seat.status())
                                    .seatPrice(seat.seatPrice())
                                    .build()).toList()
                    : Collections.emptyList();

            return SeatResponse.builder()
                    .concertId(seats.concertId())
                    .scheduleId(seats.scheduleId())
                    .concertAt(seats.concertAt())
                    .maxSeats(MAX_SEAT)
                    .seats(list)
                    .build();
        }

    }

}
