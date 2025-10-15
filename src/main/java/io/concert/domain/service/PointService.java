package io.concert.domain.service;

import io.concert.domain.model.Point;
import io.concert.domain.repository.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;

    public Point getPoint(Long userId) {
        return pointRepository.findPoint(userId);
    }

    public void usePoint(Point point, int amount) {
        Point usedPoint = point.usePoint(amount);
        pointRepository.save(usedPoint);
    }

    public Point chargePoint(Long userId, Long amount) {
        Point point = pointRepository.findPoint(userId);
        Point updatePoint = point.chargePoint(amount);
        pointRepository.save(updatePoint);
        return updatePoint;
    }
}
