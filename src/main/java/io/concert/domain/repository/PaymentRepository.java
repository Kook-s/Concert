package io.concert.domain.repository;

import io.concert.domain.model.Payment;

public interface PaymentRepository {
    Payment save(Payment payment);
}
