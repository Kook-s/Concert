package io.concert.domain.service;

import io.concert.domain.model.Queue;
import io.concert.domain.repository.QueueRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class QueueService {

    private final QueueRepository queueRepository;
    private static final long MAX_ACTIVE_TOKENS =200;

    public Queue issueToken(Long userId) {
        Long activateCount = queueRepository.getActiveTokenCount();

        Long rank = queueRepository.getWaitingTokenCount();

        Queue token = Queue.createToken(userId, activateCount, rank);

        if (token.checkStatus()) {
            queueRepository.saveActiveToken(token.token());
        } else {
            queueRepository.saveWaitingToken(token.token());
        }

        return token;
    }

    public void expireToken(String token) {
        queueRepository.removeToken(token);
    }

    public void validateToken(String token) {
        boolean exists = queueRepository.activeTokenExist(token);
        if (!exists) throw new CoreException(ErrorType.TOKEN_INVALID, "유효하지 않은 토큰입니다.");
    }

    public Queue getToken(String token) {
        return queueRepository.findToken(token);
    }

    public void updateActiveTokens() {
        long activateCount = queueRepository.getActiveTokenCount();

        if (activateCount < MAX_ACTIVE_TOKENS) {
            long neededTokens = MAX_ACTIVE_TOKENS - activateCount;
            List<Object> waitingTokens = queueRepository.getWaitingTokens(neededTokens);

            if (!waitingTokens.isEmpty()) {
                queueRepository.removeWaitingToken(new HashSet<>(waitingTokens));
                waitingTokens.forEach(queueRepository::saveActiveToken);
            }
        }
    }
}
