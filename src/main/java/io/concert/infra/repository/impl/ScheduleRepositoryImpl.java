package io.concert.infra.repository.impl;

import io.concert.domain.model.Schedule;
import io.concert.domain.repository.ScheduleRepository;
import io.concert.infra.entity.ScheduleEntity;
import io.concert.infra.enums.ConcertStatus;
import io.concert.infra.repository.jpa.ScheduleJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ScheduleRepositoryImpl implements ScheduleRepository {

    private final ScheduleJpaRepository scheduleJpaRepository;

    @Override
    public Optional<Schedule> findById(long id) {
        Optional<Schedule> findSchedule = scheduleJpaRepository.findById(id).map(ScheduleEntity::toDomain);

        return findSchedule;
    }

    @Override
    public List<Schedule> findByConcertId(long concertId) {
        return scheduleJpaRepository.findByConcertId(concertId)
                .stream()
                .map(ScheduleEntity::toDomain)
                .toList();
    }

    @Override
    public List<Schedule> findByConcertAt(LocalDateTime concertAt) {
        List<Schedule> list = scheduleJpaRepository.findByConcertAt(concertAt)
                .stream()
                .map(ScheduleEntity::toDomain)
                .toList();

        return list;
    }

    @Override
    public List<Schedule> findOpenByConcert(LocalDateTime date) {
        List<Schedule> list = scheduleJpaRepository.findByConcertAtBeforeAndStatus(date, ConcertStatus.OPEN)
                .stream()
                .map(ScheduleEntity::toDomain)
                .toList();

        return list;
    }
}
