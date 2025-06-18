package io.concert.infra.repository.impl;

import io.concert.domain.model.Reservation;
import io.concert.domain.repository.ReservationRepository;
import io.concert.infra.entity.ReservationEntity;
import io.concert.infra.repository.jpa.ReservationJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ReservationRepositoryImpl implements ReservationRepository {

    private final ReservationJpaRepository reservationJpaRepository;

    @Override
    public Optional<Reservation> findById(long id) {
        return  reservationJpaRepository.findById(id).map(ReservationEntity::toDomain);
    }

    @Override
    public Optional<Reservation> findByUserId(long id) {
        return reservationJpaRepository.findByUserId(id).map(ReservationEntity::toDomain);
    }

    @Override
    public List<Reservation> findAll() {
        return reservationJpaRepository.findAll().stream()
                .map(ReservationEntity::toDomain)
                .toList();
    }

    @Override
    public Reservation reservation(Reservation reservation) {
        Reservation result = reservationJpaRepository.save(ReservationEntity.from(reservation)).toDomain();

        return result;
    }
}
