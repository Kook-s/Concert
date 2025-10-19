package io.concert.domain.repository;

import io.concert.domain.model.Queue;

import java.util.List;
import java.util.Set;

public interface QueueRepository {

    boolean activeTokenExist(String token);

    void removeToken(String token);

    Long getActiveTokenCount();

    Long getWaitingTokenCount();

    void saveActiveToken(Object token);

    void saveWaitingToken(String token);

    List<Object> getWaitingTokens(long neededTokens);

    Queue findToken(String token);

    void removeWaitingToken(Set<Object> Strings);
}
