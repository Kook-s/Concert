package io.concert.interfaces;

import io.concert.application.ReservationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationFacade reservationFacade;


    @PostMapping


}
