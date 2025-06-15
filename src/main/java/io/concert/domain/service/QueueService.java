package io.concert.domain.service;

import io.concert.domain.model.Queue;
import io.concert.domain.repository.QueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;

    public Queue getQueue(String token) {
        return queueRepository.findQueue(token);
    }

    public Queue getQueue(long queueId) {
        return queueRepository.findQueue(queueId);
    }

    public long getActiveCount() {
        return queueRepository.findActiveCount();
    }

    public long getRemainCount(Queue queue) {
        return queueRepository.findRemainQueue(queue);
    }

    public void saveQueue(Queue queue) {
        queueRepository.saveQueue(queue);
    }

}
