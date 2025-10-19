package io.concert.domain.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.concert.domain.model.Payment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record PaymentEventCommand(
        Long id,
        Long reservationId,
        Long userId,
        int amount,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss.SSS")
        LocalDateTime paymentAt,
        String uuid
) {

    public static PaymentEventCommand from(Payment payment) {
        return PaymentEventCommand.builder()
                .id(payment.id())
                .reservationId(payment.reservationId())
                .userId(payment.userId())
                .amount(payment.amount())
                .paymentAt(payment.paymentAt())
                .uuid(UUID.randomUUID().toString())
                .build();

    }
}
