package io.concert.infra.repository.impl;

import io.concert.domain.model.Queue;
import io.concert.domain.repository.QueueRepository;
import io.concert.infra.repository.QueueJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final QueueJpaRepository queueJpaRepository;

    @Override
    public Queue findQueue(long userId) {
        return null;
    }

    @Override
    public Queue findQueue(String token) {
        return null;
    }

    @Override
    public Long findActiveCount() {
        return 0L;
    }

    @Override
    public Long findRemainQueue(Long queueId) {
        return 0L;
    }

    @Override
    public void saveQueue(Queue token) {

    }
}
