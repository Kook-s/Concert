package io.concert.interfaces;

import io.concert.application.ReservationFacade;
import io.concert.domain.model.Reservation;
import io.concert.interfaces.dto.ReservationDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;

    @PostMapping
    public ResponseEntity<ReservationDto.ReservationResponse> createReservation(
            @RequestBody ReservationDto.ReservationRequest  request
    ) {
        Reservation reservation = reservationFacade.reservation(request.userId(), request.scheduleId(), request.seatId());

        return ResponseEntity.ok(ReservationDto.ReservationResponse.of(reservation));
    }


}
