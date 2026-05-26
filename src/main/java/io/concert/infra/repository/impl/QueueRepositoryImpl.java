package io.concert.infra.repository.impl;

import io.concert.domain.model.Queue;
import io.concert.domain.repository.QueueRepository;
import io.concert.infra.redis.RedisRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.type.QueueStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class QueueRepositoryImpl implements QueueRepository {

    private final RedisRepository redisRepository;

    private static final String ACTIVE_TOKEN_KEY = "activeToken";
    private static final String WAITING_TOKEN_KEY = "waitingToken";
    private static final Duration TOKEN_TTL = Duration.ofMinutes(10);

    @Override
    public boolean activeTokenExist(String token) {
        return redisRepository.keyExists(ACTIVE_TOKEN_KEY + ":" + token);
    }

    @Override
    public Long getActiveTokenCount() {
        return redisRepository.getSize(ACTIVE_TOKEN_KEY);
    }

    @Override
    public Long getWaitingTokenCount() {
        return redisRepository.getSize(WAITING_TOKEN_KEY);
    }

    @Override
    public void saveActiveToken(Object token) {
        redisRepository.put(ACTIVE_TOKEN_KEY + ":" + token, token, TOKEN_TTL);
    }

    @Override
    public void saveWaitingToken(String token) {
        redisRepository.addSortedSet(WAITING_TOKEN_KEY, token, System.currentTimeMillis());
    }

    @Override
    public void removeToken(String token) {
        redisRepository.remove(ACTIVE_TOKEN_KEY + ":" + token);
        redisRepository.removeSortedSetMembers(WAITING_TOKEN_KEY, Set.of(token));
    }

    @Override
    public List<Object> getWaitingTokens(long neededTokens) {
        Set<Object> tokens = redisRepository.getSortedSetRange(WAITING_TOKEN_KEY, 0, neededTokens - 1);

        if (tokens != null && !tokens.isEmpty()) {
            return tokens.stream().toList();
        }

        return List.of();
    }

    @Override
    public Queue findToken(String token) {

        Object activeToken = redisRepository.get(ACTIVE_TOKEN_KEY + ":" + token);

        if(activeToken != null) {
            return Queue.builder().token(token).status(QueueStatus.ACTIVE).build();
        }

        Long waitingRank = redisRepository.getSortedSetRank(WAITING_TOKEN_KEY, token);
        if(waitingRank != null) {
            return Queue.builder().token(token).rank(waitingRank + 1).status(QueueStatus.WAITING).build();
        }

        throw new CoreException(ErrorType.RESOURCE_NOT_FOUND, "토큰" + token);
    }

    @Override
    public void removeWaitingToken(Set<Object> Strings) {
        redisRepository.removeSortedSetMembers(WAITING_TOKEN_KEY, Strings);
    }
}
