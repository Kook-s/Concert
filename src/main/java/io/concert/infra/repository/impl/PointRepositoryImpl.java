package io.concert.infra.repository.impl;

import io.concert.domain.model.Point;
import io.concert.domain.repository.PointRepository;
import io.concert.infra.entity.PointEntity;
import io.concert.infra.repository.jpa.PointJpaRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

    private final PointJpaRepository pointJpaRepository;

    @Override
    public Point findPoint(Long userId) {
        return pointJpaRepository.findByUserId(userId)
                .map(PointEntity::of)
                .orElseThrow(() -> new CoreException(ErrorType.RESOURCE_NOT_FOUND, "사용자 ID: " + userId));
    }

    @Override
    public void save(Point updatePoint) {
        pointJpaRepository.save(PointEntity.from(updatePoint));
    }
}
