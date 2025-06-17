package io.concert.domain.service;

import io.concert.domain.model.Seat;
import io.concert.domain.repository.SeatRepository;
import io.concert.support.CustomException;
import io.concert.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SeatService {

    private final SeatRepository seatRepository;

    public Seat getSeat(long id) {
        return seatRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.SEAT_NOT_FOUND));

    }

    public List<Seat> getAllSeats(long scheduleId) {
        return seatRepository.findByScheduleId(scheduleId);
    }

    public List<Seat> getAvailableSeats(long scheduleId) {
        return seatRepository.findByScheduleIdAndStatus(scheduleId);
    }
}
