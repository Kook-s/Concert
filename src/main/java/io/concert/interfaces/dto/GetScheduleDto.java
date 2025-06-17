package io.concert.interfaces.dto;

import io.concert.domain.model.Schedule;
import lombok.Builder;

import java.util.List;

public class GetScheduleDto {

    @Builder
    public record ScheduleResponse(
            List<ScheduleDto> schedules
    ) {

        public static ScheduleResponse of(List<Schedule> schedules) {
            List<ScheduleDto> list = schedules.stream()
                    .map(schedule -> ScheduleDto.builder()
                            .id(schedule.id())
                            .concertId(schedule.concertId())
                            .status(schedule.status())
                            .concertAt(schedule.concertAt())
                            .reservationAt(schedule.reservationAt())
                            .deadlineAt(schedule.deadlineAt())
                            .build()
                    )
                    .toList();

            return ScheduleResponse.builder()
                    .schedules(list)
                    .build();
        }

    }
}
