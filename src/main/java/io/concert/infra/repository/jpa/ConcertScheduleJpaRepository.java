package io.concert.infra.repository.jpa;

import io.concert.infra.entity.ConcertScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ConcertScheduleJpaRepository extends JpaRepository<ConcertScheduleEntity, Long> {
    List<ConcertScheduleEntity> findByConcertIdAndReservationAtBeforeAndDeadlineAfter(Long concertId, LocalDateTime now, LocalDateTime now1);
}
