package io.concert.application.facade;

import io.concert.domain.model.Payment;
import io.concert.domain.model.Point;
import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.service.ConcertService;
import io.concert.domain.service.PaymentService;
import io.concert.domain.service.PointService;
import io.concert.domain.service.ReservationService;
import io.concert.support.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final PointService pointService;
    private final ConcertService concertService;

    public Payment processPayment(String lockName, String token, Long reservationId, Long userId) {
        Reservation reservation = reservationService.checkReservation(reservationId, userId);
        Seat seat = concertService.getSeat(reservation.seatId());
        Point point = pointService.getPoint(userId);

        pointService.usePoint(point, seat.seatPrice());

        Reservation reserved = reservationService.changeStatus(reservation, ReservationStatus.COMPLETED);

        Payment bill = paymentService.createBill(reservationId, userId, seat.seatPrice());


        return bill;
    }

}
