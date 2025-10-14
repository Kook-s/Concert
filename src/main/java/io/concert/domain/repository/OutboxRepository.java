package io.concert.domain.repository;

import io.concert.domain.model.OutboxEvent;

import java.util.List;

public interface OutboxRepository {

    void save(OutboxEvent event);

    OutboxEvent findByUuid(String uuid);

    List<OutboxEvent> findByStatusNot(String status);
}
