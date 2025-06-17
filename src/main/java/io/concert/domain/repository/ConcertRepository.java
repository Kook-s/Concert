package io.concert.domain.repository;

import io.concert.domain.model.Concert;

import java.util.List;
import java.util.Optional;

public interface ConcertRepository {

    List<Concert> concertList();
    Optional<Concert> findById(long id);

}
