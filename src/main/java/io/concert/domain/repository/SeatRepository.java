package io.concert.domain.repository;

import io.concert.domain.model.Seat;
import io.concert.infra.enums.SeatStatus;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {

    Optional<Seat> findById(Long id);
    List<Seat> findByScheduleId(Long id);
    List<Seat> findByScheduleIdAndStatus(long scheduleId);
    Seat reservationSeat(Seat seat);
}
