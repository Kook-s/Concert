package io.concert.domain.repository;

import io.concert.domain.model.Concert;

import java.util.Optional;

public interface ConcertRepository {

    Optional<Concert> findById(long id);

}
