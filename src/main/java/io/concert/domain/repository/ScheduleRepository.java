package io.concert.domain.repository;

import io.concert.domain.model.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    Optional<Schedule> findById(long id);
    List<Schedule> findByConcertId(long concertId);
    List<Schedule> findByConcertAt(LocalDateTime date);
    List<Schedule> findOpenByConcert(LocalDateTime date);


}
