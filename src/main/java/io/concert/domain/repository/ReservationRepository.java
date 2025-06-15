package io.concert.domain.repository;

import io.concert.domain.model.Reservation;

import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> findById(long id);

}
