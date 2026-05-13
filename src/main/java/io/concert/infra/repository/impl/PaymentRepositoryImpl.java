package io.concert.infra.repository.impl;

import io.concert.domain.model.Payment;
import io.concert.domain.repository.PaymentRepository;
import io.concert.infra.entity.PaymentEntity;
import io.concert.infra.entity.ReservationEntity;
import io.concert.infra.entity.UserEntity;
import io.concert.infra.repository.jpa.PaymentJpaRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;
    private final EntityManager entityManager;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentJpaRepository.save(
                PaymentEntity.builder()
                        .id(payment.id())
                        .reservation(entityManager.getReference(ReservationEntity.class, payment.reservationId()))
                        .user(entityManager.getReference(UserEntity.class, payment.userId()))
                        .amount(payment.amount())
                        .paymentAt(payment.paymentAt())
                        .build()
        );
        return entity.of();
    }
}
