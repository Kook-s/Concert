package io.concert.infra.repository.jpa;

import io.concert.infra.entity.ReservationEntity;
import io.concert.support.type.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

//    Optional<ReservationEntity> findByReservationId(Long reservationId);

    List<ReservationEntity> findByStatusAndReservationAtBefore(ReservationStatus status, LocalDateTime localDateTime);

    List<ReservationEntity> findByConcertIdAndScheduleIdAndSeatId(Long concertId, Long scheduleId, Long seatId);
}
