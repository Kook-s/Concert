package io.concert.infra.repository.impl;

import io.concert.domain.model.Payment;
import io.concert.domain.repository.PaymentRepository;
import io.concert.infra.entity.PaymentEntity;
import io.concert.infra.repository.jpa.PaymentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

    private final PaymentJpaRepository paymentJpaRepository;

    @Override
    public Payment save(Payment payment) {
        PaymentEntity entity = paymentJpaRepository.save(PaymentEntity.from(payment));
        return entity.of();
    }
}
