package io.concert.interfaces.dto;

import io.concert.domain.model.ConcertSchedule;
import lombok.Builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GetScheduleDto {

    @Builder
    public record ScheduleResponse(
            Long concertId,
            List<ScheduleDto> schedules
    ) {

        public static ScheduleResponse of(Long concertId, List<ConcertSchedule> schedules) {
            List<ScheduleDto> list = (schedules != null) ?
                    schedules.stream()
                            .map( schedule ->
                                    ScheduleDto.builder()
                                            .scheduleId(schedule.id())
                                            .concertAt(schedule.concertAt())
                                            .reservationAt(schedule.reservationAt())
                                            .deadline(schedule.deadline())
                                            .build())
                            .toList()
                    : Collections.emptyList();

            return ScheduleResponse.builder()
                    .concertId(concertId)
                    .schedules(list)
                    .build();
        }

    }
}
