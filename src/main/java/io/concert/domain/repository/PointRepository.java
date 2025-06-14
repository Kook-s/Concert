package io.concert.domain.repository;

import io.concert.domain.model.Point;
import io.concert.infra.entity.PointEntity;

import java.util.Optional;

public interface PointRepository {

    public Optional<Point> findByUserId(long userId);
    public void charge(long userId, int amount);
}
