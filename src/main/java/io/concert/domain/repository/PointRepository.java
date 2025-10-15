package io.concert.domain.repository;

import io.concert.domain.model.Point;

public interface PointRepository {
    Point findPoint(Long userId);

    void save(Point updatePoint);
}
