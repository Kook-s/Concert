package io.concert.infra.repository.jpa;

import io.concert.infra.entity.SeatEntity;
import io.concert.support.type.SeatStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {

    @Query("SELECT s FROM seat s " +
            "JOIN s.concertSchedule cs " +
            "JOIN cs.concert c " +
            "WHERE cs.id = :scheduleId " +
            "AND c.id = :concertId " +
            "AND s.status = :seatStatus")
    List<SeatEntity> findSeat(
            @Param("concertId") Long concertId,
            @Param("scheduleId") Long scheduleId,
            @Param("seatStatus") SeatStatus seatStatus);


}
