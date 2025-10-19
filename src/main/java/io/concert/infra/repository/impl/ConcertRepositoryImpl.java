package io.concert.infra.repository.impl;

import io.concert.domain.model.Concert;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ConcertRepository;
import io.concert.infra.entity.ConcertEntity;
import io.concert.infra.entity.ConcertScheduleEntity;
import io.concert.infra.entity.SeatEntity;
import io.concert.infra.repository.jpa.ConcertJpaRepository;
import io.concert.infra.repository.jpa.ConcertScheduleJpaRepository;
import io.concert.infra.repository.jpa.SeatJpaRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.type.SeatStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;
    private final ConcertScheduleJpaRepository concertScheduleJpaRepository;
    private final SeatJpaRepository seatJpaRepository;

    @Override
    public List<Concert> findConcerts() {
        return concertJpaRepository.findAll()
                .stream()
                .map(ConcertEntity::of)
                .toList();
    }

    @Override
    public List<ConcertSchedule> findConcertSchedules(Long concertId) {
        LocalDateTime now = LocalDateTime.now();
        return concertScheduleJpaRepository.findByConcertIdAndReservationAtBeforeAndDeadlineAfter(concertId, now, now)
                .stream()
                .map(ConcertScheduleEntity::of)
                .toList();
    }

    @Override
    public Concert findConcert(Long concertId) {
        return concertJpaRepository.findById(concertId)
                .map(ConcertEntity::of)
                .orElseThrow(() -> new CoreException(ErrorType.RESOURCE_NOT_FOUND, "검색한 콘서트 ID: " + concertId));
    }

    @Override
    public List<Seat> findSeats(Long concertId, Long scheduleId, SeatStatus seatStatus) {
        return seatJpaRepository.findSeat(concertId, scheduleId, seatStatus)
                .stream()
                .map(SeatEntity::of)
                .toList();
    }

    @Override
    public ConcertSchedule findConcertSchedule(Long scheduleId) {
        return concertScheduleJpaRepository.findById(scheduleId)
                .map(ConcertScheduleEntity::of)
                .orElseThrow(() -> new CoreException(ErrorType.RESOURCE_NOT_FOUND, "검색한 일정 ID: " + scheduleId));
    }

    @Override
    public void saveSeat(Seat seat) {
        seatJpaRepository.save(SeatEntity.from(seat));
    }

    @Override
    public Seat findSeat(Long seatId) {
        return seatJpaRepository.findById(seatId)
                .map(SeatEntity::of)
                .orElseThrow(() -> new CoreException(ErrorType.RESOURCE_NOT_FOUND, "검색한 좌석 ID: " + seatId));
    }

}
