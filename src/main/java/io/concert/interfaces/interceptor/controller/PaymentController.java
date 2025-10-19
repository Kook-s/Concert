package io.concert.interfaces.interceptor.controller;

import io.concert.application.facade.PaymentFacade;
import io.concert.domain.model.Payment;
import io.concert.interfaces.dto.PaymentDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentFacade paymentFacade;

    /**
     * 결제 요청
     */
    @PostMapping
    public ResponseEntity<PaymentDto.PaymentResponse> payment(
            @RequestHeader("Token") String token,
            @Valid @RequestBody PaymentDto.PaymentRequest request
    ) {
        Payment payment = paymentFacade.processPayment("userId:" + request.userId(), token, request.reservationId(), request.userId());
        return ResponseEntity.ok(PaymentDto.PaymentResponse.of(payment));
    }
}
