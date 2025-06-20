package io.concert.infra.repository.jpa;

import io.concert.infra.entity.QueueEntity;
import io.concert.infra.enums.QueueStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface QueueJpaRepository extends JpaRepository<QueueEntity, Long> {

    Optional<QueueEntity> findByUserId(long userId);
    Optional<QueueEntity> findByToken(String token);
    Long countByStatus(QueueStatus status);
    Long countByCreatedAtBeforeAndStatus(LocalDateTime createAt, QueueStatus status);

}
