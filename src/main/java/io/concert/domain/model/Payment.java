package io.concert.domain.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record Payment(
        Long id,
        Long reservationId,
        Long userId,
        int amount,
        LocalDateTime paymentAt
) {
    public static Payment create(Long reservationId, Long userId, int amount) {
        return Payment.builder()
                .reservationId(reservationId)
                .userId(userId)
                .amount(amount)
                .paymentAt(LocalDateTime.now())
                .build();
    }

}
