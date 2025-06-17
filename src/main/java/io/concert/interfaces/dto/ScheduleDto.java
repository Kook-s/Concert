package io.concert.interfaces.dto;

import io.concert.infra.enums.ConcertStatus;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ScheduleDto(
        long id,
        long concertId,
        ConcertStatus status,
        LocalDateTime concertAt,
        LocalDateTime reservationAt,
        LocalDateTime deadlineAt
) {
}
