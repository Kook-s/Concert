package io.concert.domain.repository;

import io.concert.domain.model.Queue;

public interface QueueRepository {
    Queue findQueue(long userId);
    Queue findQueue(String token);

    Long findActiveCount();
    Long findRemainQueue(Long queueId);
    void saveQueue(Queue token);

}
