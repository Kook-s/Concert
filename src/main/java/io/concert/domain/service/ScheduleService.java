package io.concert.domain.service;

import io.concert.domain.model.Schedule;
import io.concert.domain.repository.ScheduleRepository;
import io.concert.support.CustomException;
import io.concert.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public Schedule getSchedule(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_FOUND));
    }

    public List<Schedule> getConcertSchedules(long concertId) {
        return scheduleRepository.findByConcertId(concertId);
    }

    public List<Schedule> getSchedules(LocalDateTime concertAt) {
        return scheduleRepository.findByConcertAt(concertAt);
    }

    public List<Schedule> getOpenSchedules(LocalDateTime concertAt) {
        return scheduleRepository.findOpenByConcert(concertAt);
    }
}
