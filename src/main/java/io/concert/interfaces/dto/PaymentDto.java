package io.concert.interfaces.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.concert.domain.model.Payment;
import lombok.Builder;

import java.time.LocalDateTime;

public class PaymentDto {

    @Builder
    public record PaymentRequest(
            Long userId,
            Long reservationId
    ) {
    }

    @Builder
    public record PaymentResponse(
            Long paymentId,
            int amount,
            @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
            LocalDateTime paymentAt
    ) {
        public static PaymentResponse of(Payment payment) {
            return PaymentResponse.builder()
                    .paymentId(payment.id())
                    .amount(payment.amount())
                    .paymentAt(payment.paymentAt())
                    .build();
        }
    }
}
