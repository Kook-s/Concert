package io.concert.infra.repository.jpa;

import io.concert.domain.model.Seat;
import io.concert.infra.entity.SeatEntity;
import io.concert.infra.enums.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {
    List<SeatEntity> findByScheduleId(long scheduleId);
    List<SeatEntity> findByScheduleIdAndStatus(long scheduleId, SeatStatus status);

}
