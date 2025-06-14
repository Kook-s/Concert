package io.concert.domain.service;

import io.concert.domain.model.Point;
import io.concert.domain.repository.PointRepository;
import io.concert.support.CustomException;
import io.concert.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Point getPoint(long userId) {
        Point findPoint = pointRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.POINT_NOT_FOUND));
        return findPoint;
    }

    @Transactional
    public void chargePoint(long userId, int amount) {
        pointRepository.charge(userId, amount);
    }
}
