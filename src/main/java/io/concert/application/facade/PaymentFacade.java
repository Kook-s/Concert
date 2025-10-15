package io.concert.application.facade;

import io.concert.domain.dto.PaymentEventCommand;
import io.concert.domain.model.Payment;
import io.concert.domain.model.Point;
import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.PaymentRepository;
import io.concert.domain.service.*;
import io.concert.support.aop.DistributedLock;
import io.concert.support.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentFacade {

    private final QueueService queueService;
    private final ReservationService reservationService;
    private final PaymentService paymentService;
    private final PointService pointService;
    private final ConcertService concertService;
    private final PaymentRepository paymentRepository;
    private final PaymentEventService paymentEventService;

    @DistributedLock(key = "#lockName")
    public Payment processPayment(String lockName, String token, Long reservationId, Long userId) {
        Reservation reservation = reservationService.checkReservation(reservationId, userId);
        Seat seat = concertService.getSeat(reservation.seatId());
        Point point = pointService.getPoint(userId);

        pointService.usePoint(point, seat.seatPrice());

        Reservation reserved = reservationService.changeStatus(reservation, ReservationStatus.COMPLETED);

        queueService.expireToken(token);

        Payment bill = paymentService.createBill(reservationId, userId, seat.seatPrice());


        paymentEventService.publicEvent(PaymentEventCommand.from(bill));

        return bill;
    }

}
