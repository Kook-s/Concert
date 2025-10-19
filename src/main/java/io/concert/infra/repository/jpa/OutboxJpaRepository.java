package io.concert.infra.repository.jpa;

import io.concert.infra.entity.OutboxEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OutboxJpaRepository extends JpaRepository<OutboxEntity, Long> {
    Optional<OutboxEntity> findByUuid(String uuid);
    List<OutboxEntity> findByStatusNot(String status);
}
