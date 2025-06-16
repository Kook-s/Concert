package io.concert.infra.repository.impl;

import io.concert.domain.model.Concert;
import io.concert.domain.repository.ConcertRepository;
import io.concert.infra.entity.ConcertEntity;
import io.concert.infra.repository.jpa.ConcertJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ConcertRepositoryImpl implements ConcertRepository {

    private final ConcertJpaRepository concertJpaRepository;

    @Override
    public Optional<Concert> findById(long id) {
        return concertJpaRepository.findById(id)
                .map(ConcertEntity::toDomain);
    }


}
