package io.concert.application.scheduler;

import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ConcertRepository;
import io.concert.domain.repository.ReservationRepository;
import io.concert.support.type.ReservationStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SeatScheduler {

    private final ConcertRepository concertRepository;
    private final ReservationRepository reservationRepository;

    @Transactional
    @Scheduled(cron = "0 * * * * *")
    public void manageAvailableSeats() {
        List<Reservation> unpaidReservations =
                reservationRepository.findExpiredReservation(
                        ReservationStatus.PAYMENT_WAITING,
                        LocalDateTime.now().minusMinutes(5)
                );

        for (Reservation unpaidReservation : unpaidReservations) {
            Seat seat = concertRepository.findSeat(unpaidReservation.seatId());

            Seat updateSeat = seat.toAvailable();

            if (updateSeat != null) {
                concertRepository.saveSeat(updateSeat);
            }

            Reservation expiredReservation = unpaidReservation.changeStatus(ReservationStatus.EXPIRED);
            reservationRepository.save(expiredReservation);
        }
    }
}
