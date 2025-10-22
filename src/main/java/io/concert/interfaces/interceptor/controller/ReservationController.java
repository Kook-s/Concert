package io.concert.interfaces.interceptor.controller;

import io.concert.application.dto.ReservationResult;
import io.concert.application.facade.ReservationFacade;
import io.concert.interfaces.dto.ReservationDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @PostMapping
    public ResponseEntity<ReservationDto.ReservationResponse> createReservation(
            @RequestHeader("Token") String token,
            @Valid @RequestBody ReservationDto.ReservationRequest request
    ) {
        ReservationResult reservation = reservationFacade
                .reservation("RESERVATION:" + request.seatId(), request.toCommand(token));
        return ok(ReservationDto.ReservationResponse.of(reservation));

    }
}
