package io.concert.infra.repository.jpa;

import io.concert.infra.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {

    Optional<PointEntity> findByUserId(Long userId);
}
