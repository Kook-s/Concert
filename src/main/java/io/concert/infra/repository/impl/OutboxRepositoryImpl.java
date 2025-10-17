package io.concert.infra.repository.impl;

import io.concert.domain.model.OutboxEvent;
import io.concert.domain.repository.OutboxRepository;
import io.concert.infra.entity.OutboxEntity;
import io.concert.infra.repository.jpa.OutboxJpaRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OutboxRepositoryImpl implements OutboxRepository {

    private final OutboxJpaRepository outboxJpaRepository;

    @Override
    public void save(OutboxEvent event) {
        outboxJpaRepository.save(OutboxEntity.from(event));
    }

    @Override
    public OutboxEvent findByUuid(String uuid) {
        return outboxJpaRepository.findByUuid(uuid)
                .map(OutboxEntity::of)
                .orElseThrow(() -> new CoreException(ErrorType.RESOURCE_NOT_FOUND, "uuid: " + uuid));
    }

    @Override
    public List<OutboxEvent> findByStatusNot(String status) {
        return outboxJpaRepository.findByStatusNot(status).stream()
                .map(OutboxEntity::of)
                .toList();
    }
}
