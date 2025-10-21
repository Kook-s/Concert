package io.concert.domain.component;

import io.concert.application.scheduler.SeatScheduler;
import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ConcertRepository;
import io.concert.domain.repository.ReservationRepository;
import io.concert.support.type.ReservationStatus;
import io.concert.support.type.SeatStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class SeatSchedulerTest {

    @Autowired
    private SeatScheduler seatScheduler;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private Reservation reservation;
    private Seat seat;

    @BeforeEach
    void setUp() {
        // 예약 및 좌석 데이터 세팅
        seat = Seat.builder()
                .id(1L)
                .concertScheduleId(1L)
                .seatNo(1)
                .status(SeatStatus.UNAVAILABLE)
                .reservationAt(LocalDateTime.now().minusMinutes(6)) // 예약한지 5분 이상 지난 상태
                .seatPrice(5000)
                .build();

        reservation = Reservation.builder()
                .id(1L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(seat.id())
                .userId(1L)
                .status(ReservationStatus.PAYMENT_WAITING)
                .reservationAt(LocalDateTime.now().minusMinutes(6)) // 5분 넘게 결제 대기 중
                .build();

        concertRepository.saveSeat(seat);  // 좌석 저장
        reservationRepository.save(reservation);  // 예약 저장
    }

    @Test
    @Transactional
    void 예약후_5분_이상_지났지만_결제되지_않은_경우_좌석을_이용_가능_상태로_변경한다() {
        // when
        seatScheduler.manageAvailableSeats();  // 좌석 상태 변경 처리

        // then
        // 1. 좌석 상태가 AVAILABLE로 변경되었는지 확인
        Seat updatedSeat = concertRepository.findSeat(seat.id());
        assertThat(updatedSeat.status()).isEqualTo(SeatStatus.AVAILABLE);

        // 2. 예약 상태가 EXPIRED로 변경되었는지 확인
        Reservation updatedReservation = reservationRepository.findById(reservation.id());
        assertThat(updatedReservation.status()).isEqualTo(ReservationStatus.EXPIRED);
    }
}