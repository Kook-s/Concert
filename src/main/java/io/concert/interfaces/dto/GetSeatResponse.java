package io.concert.interfaces.dto;

import io.concert.domain.model.Seat;
import lombok.Builder;

import java.util.List;

public class GetSeatResponse {

    @Builder
    public record SeatResponse(
            List<SeatDto> seats
    ){

        public static SeatResponse of(List<Seat> seats) {
            List<SeatDto> list = seats.stream()
                    .map(seat -> SeatDto.builder()
                            .id(seat.id())
                            .seatNo(seat.seatNo())
                            .status(seat.status())
                            .seatPrice(seat.seatPrice())
                            .scheduleId(seat.scheduleId())
                            .reservationId(seat.reservationId())
                            .build()
                    )
                    .toList();

            return SeatResponse.builder()
                    .seats(list)
                    .build();
        }

    }
}
