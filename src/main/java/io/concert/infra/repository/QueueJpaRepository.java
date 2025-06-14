package io.concert.infra.repository;

import io.concert.infra.entity.QueueEntity;
import io.concert.infra.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<QueueEntity, Long> {

    Optional<QueueEntity> findByToken(String token);
    Long countByStatus(QueueStatus status);

}
