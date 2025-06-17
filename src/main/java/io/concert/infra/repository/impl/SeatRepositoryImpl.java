package io.concert.infra.repository.impl;

import io.concert.domain.model.Seat;
import io.concert.domain.repository.SeatRepository;
import io.concert.infra.entity.SeatEntity;
import io.concert.infra.enums.SeatStatus;
import io.concert.infra.repository.jpa.SeatJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SeatRepositoryImpl implements SeatRepository {

    private final SeatJpaRepository seatJpaRepository;

    @Override
    public Optional<Seat> findById(Long id) {
        return seatJpaRepository.findById(id)
                .map(SeatEntity::toDomain);
    }

    @Override
    public List<Seat> findByScheduleId(Long id) {
        return seatJpaRepository.findByScheduleId(id)
                .stream()
                .map(SeatEntity::toDomain)
                .toList();

    }

    @Override
    public List<Seat> findByScheduleIdAndStatus(long scheduleId) {
        return seatJpaRepository.findByScheduleIdAndStatus(scheduleId, SeatStatus.AVAILABLE)
                .stream()
                .map(SeatEntity::toDomain)
                .toList();
    }

    @Override
    public Seat reservationSeat(Seat seat) {
        Seat result = seatJpaRepository.save(SeatEntity.from(seat)).toDomain();

        return result;
    }
}
