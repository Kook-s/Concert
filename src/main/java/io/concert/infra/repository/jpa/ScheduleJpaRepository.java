package io.concert.infra.repository.jpa;

import io.concert.infra.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleJpaRepository extends JpaRepository<ScheduleEntity, Long> {

    List<ScheduleEntity> findByConcertAt(LocalDateTime concertAt);
    List<ScheduleEntity> findByConcertAtBefore();
}
