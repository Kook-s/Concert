package io.concert.infra.repository.impl;

import io.concert.domain.model.Queue;
import io.concert.domain.repository.QueueRepository;
import io.concert.infra.entity.QueueEntity;
import io.concert.infra.enums.QueueStatus;
import io.concert.infra.repository.QueueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    // 특정 유저가 현재 발급받은 대기열 정보 조회
    @Override
    public Queue findQueue(long userId) {
        return queueJpaRepository.findByUserId(userId)
                .map(QueueEntity::toDomain)
                .orElse(null);
    }

    // 토큰을 기반으로 대기열 정보 조회
    @Override
    public Queue findQueue(String token) {
        return queueJpaRepository.findByToken(token)
                .map(QueueEntity::toDomain)
                .orElse(null);
    }

    // 현재 대기열 존재하는 유저 수
    @Override
    public Long findActiveCount() {
        return queueJpaRepository.countByStatus(QueueStatus.ACTIVE);
    }

    // 현재 대기자 수
    @Override
    public Long findRemainQueue(Queue queue) {
        QueueEntity findQueueEntity = QueueEntity.from(queue);

        return queueJpaRepository.countByCreatedAtBeforeAndStatus(
                findQueueEntity.getCreatedAt(),
                QueueStatus.WAITING
        );
    }

    @Override
    public void saveQueue(Queue queue) {
        QueueEntity saveQueue = QueueEntity.from(queue);

        queueJpaRepository.save(saveQueue);
    }
}
