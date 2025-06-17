package io.concert.domain.repository;

import io.concert.domain.model.Reservation;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {

    Optional<Reservation> findById(String id);
    List<Reservation> findAll();
    Optional<Reservation> reservation(Reservation reservation);
}
