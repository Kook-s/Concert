package io.concert.interfaces.dto;

import io.concert.domain.model.Reservation;
import io.concert.infra.enums.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

public class ReservationDto {

    @Builder
    public record ReservationRequest(
            Long userId,
            Long scheduleId,
            Long seatId
    ) {
        public ReservationCommand toCommand() {
            return ReservationCommand.builder()
                    .userId(this.userId)
                    .scheduleId(this.scheduleId)
                    .seatId(this.seatId)
                    .build();
        }
    }

    @Builder
    public record ReservationResponse(
            Long reservationId,
            Long scheduleId,
            Long userId,
            ReservationStatus status,
            LocalDateTime reservationAt
    ) {
        public static ReservationResponse of(Reservation reservation) {
            return ReservationResponse.builder()
                    .reservationId(reservation.id())
                    .scheduleId(reservation.scheduleId())
                    .userId(reservation.userId())
                    .status(reservation.status())
                    .reservationAt(reservation.reservationAt())
                    .build();
        }

    }



}
