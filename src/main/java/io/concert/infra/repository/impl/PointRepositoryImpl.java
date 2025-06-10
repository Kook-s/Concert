package io.concert.infra.repository.impl;

import io.concert.domain.model.Point;
import io.concert.domain.repository.PointRepository;
import io.concert.infra.entity.PointEntity;
import io.concert.infra.repository.PointJpaRepository;
import io.concert.support.CustomException;
import io.concert.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Optional<Point> findByUserId(long userId) {
        return pointJpaRepository.findByUserId(userId).map(PointEntity::toPoint);
    }

    @Override
    public void charge(long userId, int amount) {
        PointEntity findPoint = pointJpaRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        findPoint.increaseAmount(amount);
    }
}
