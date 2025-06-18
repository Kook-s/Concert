package io.concert.domain.repository;

import io.concert.domain.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> findById(long id);
    Optional<Reservation> findByUserId(long id);
    List<Reservation> findAll();
    Reservation reservation(Reservation reservation);
}
