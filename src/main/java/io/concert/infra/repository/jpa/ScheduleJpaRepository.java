package io.concert.infra.repository.jpa;

import io.concert.domain.model.Schedule;
import io.concert.infra.entity.ScheduleEntity;
import io.concert.infra.enums.ConcertStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findByConcertAt(LocalDateTime concertAt);
    List<ScheduleEntity> findByConcertAtBefore();
    List<ScheduleEntity> findByConcertAtBeforeAndStatus(LocalDateTime concertAt, ConcertStatus status);

    List<ScheduleEntity> findByConcertId(long concertId);
}
