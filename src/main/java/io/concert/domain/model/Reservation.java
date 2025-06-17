package io.concert.domain.model;

import io.concert.infra.enums.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Reservation(
        long id,
        long userId,
        long scheduleId,
        ReservationStatus status,
        LocalDateTime reservationAt
) {

    public static Reservation create(long userId, long scheduleId) {
        return Reservation.builder()
                .userId(userId)
                .scheduleId(scheduleId)
                .status(ReservationStatus.COMPLETED)
                .reservationAt(LocalDateTime.now())
                .build();
    }


}
