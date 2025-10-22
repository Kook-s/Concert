package io.concert.application.facade;

import io.concert.application.dto.ReservationCommand;
import io.concert.application.dto.ReservationResult;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Reservation;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ConcertRepository;
import io.concert.domain.repository.ReservationRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.type.ReservationStatus;
import io.concert.support.type.SeatStatus;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class ReservationFacadeIntegrationTest {

    Logger logger = LoggerFactory.getLogger(ReservationFacadeIntegrationTest.class);

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private ConcertRepository concertRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    private final Long USER_ID = 1L;

    @Test
    @Transactional
    void 예약_가능_시간_이전에_예약_요청_시_BEFORE_RESERVATION_AT_에러를_반환한다() {
        // given
        ConcertSchedule beforeReservationAtSchedule =
                concertRepository.findConcertSchedule(3L);
        ReservationCommand command = new ReservationCommand(USER_ID, 1L, beforeReservationAtSchedule.id(), 1L);

        // when & then
        assertThatThrownBy(() -> reservationFacade.reservation("SEAT:" + 1L, command))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.BEFORE_RESERVATION_AT.getMessage());

    }
    
    @Test
    @Transactional
    void 예약_마감_시간_이후에_예약_요청_시_AFTER_DEADLINE_에러를_반환한다() {
        // given
        ConcertSchedule afterDeadlineSchedule =
                concertRepository.findConcertSchedule(4L);
        ReservationCommand command = new ReservationCommand(USER_ID, 1L, afterDeadlineSchedule.id(), 1L);

        // when & then
        assertThatThrownBy(() -> reservationFacade.reservation("SEAT:" + 1L, command))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.AFTER_DEADLINE.getMessage());
    }
    
    @Test
    @Transactional
    void 좌석의_상태가_UNAVAILABLE_이라면_SEAT_UNAVAILABLE_에러를_반환한다() {
        // given
        ConcertSchedule schedule = concertRepository.findConcertSchedule(1L); // 예약 가능한 콘서트 일정
        Seat seat = concertRepository.findSeat(26L); // 상태가 UNAVAILABLE 인 좌석
        ReservationCommand command = new ReservationCommand(USER_ID, 1L, schedule.id(), seat.id());

        // when & then
        assertThatThrownBy(() -> reservationFacade.reservation("SEAT:" + 1L, command))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.SEAT_UNAVAILABLE.getMessage());
    }
    
    @Test
    @Transactional
    void 예약_가능한_시간이고_좌석이_예약_가능_상태라면_예약_정보를_생성하고_반환한다() {
        // given
        ConcertSchedule schedule = concertRepository.findConcertSchedule(1L); // 예약 가능한 콘서트 일정
        Seat seat = concertRepository.findSeat(1L); // 상태가 AVAILABLE 인 좌석
        ReservationCommand command = new ReservationCommand(USER_ID, 1L, schedule.id(), seat.id());

        // when
        ReservationResult reservation = reservationFacade.reservation("SEAT:" + 1L, command);

        // then
        assertThat(reservation.status()).isEqualTo(ReservationStatus.PAYMENT_WAITING); // 결제 대기 상태로 변경되었는지 검증
    }

    @Test
    void 분산락_다수의_사용자가_1개의_좌석을_동시에_예약하면_한_명만_성공한다() throws InterruptedException {
        // when
        final int threadCount = 10;
        final ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (long l = 1; l <= threadCount; l++) {
            // 좌석 예약 요청 객체 생성
            ReservationCommand command = ReservationCommand.builder()
                    .userId(l)
                    .concertId(1L)
                    .scheduleId(1L)
                    .seatId(1L)
                    .build();

            executorService.submit(() -> {
                try {
                    // 좌석 예약 호출
                    reservationFacade.reservation("SEAT:" + 1L, command);
                } catch (Exception e) {
                    logger.warn(e.getMessage());
                } finally {
                    countDownLatch.countDown();
                }
            });

        }
        countDownLatch.await();
        List<Reservation> reservations = reservationRepository.findByConcertIdAndScheduleIdAndSeatId(1L, 1L, 1L);
        // 같은 콘서트 같은 일정 같은 좌석으로 예약이 하나만 잡혔는지 검증한다.
        assertThat(reservations.size()).isOne();
        // 해당 좌석이 UNAVAILABLE 상태로 변경되었는지 검증한다.
        assertThat(concertRepository.findSeat(1L).status()).isEqualTo(SeatStatus.UNAVAILABLE);
    }
}