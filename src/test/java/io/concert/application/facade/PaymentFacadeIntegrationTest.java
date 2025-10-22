package io.concert.application.facade;

import io.concert.domain.model.Payment;
import io.concert.domain.model.Point;
import io.concert.domain.model.Queue;
import io.concert.domain.model.Reservation;
import io.concert.domain.repository.PaymentRepository;
import io.concert.domain.repository.ReservationRepository;
import io.concert.domain.service.ConcertService;
import io.concert.domain.service.PointService;
import io.concert.domain.service.QueueService;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.type.ReservationStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class PaymentFacadeIntegrationTest {

    Logger logger = LoggerFactory.getLogger(PaymentFacadeIntegrationTest.class);

    @Autowired
    private PaymentFacade paymentFacade;

    @Autowired
    private QueueService queueService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ConcertService concertService;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    private final Long USER_ID = 1L;
    private Reservation reservation;
    private String token;

    @BeforeEach
    void setUp() {
        Queue queue = queueService.issueToken(USER_ID);
        token = queue.token(); // 토큰 검증 통과를 위한 토큰 생성

        reservation = Reservation.builder()
                .id(1L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(USER_ID)
                .status(ReservationStatus.PAYMENT_WAITING)
                .reservationAt(LocalDateTime.now())
                .build();
        reservationRepository.save(reservation); // 테스트에서 사용할 예약을 저장한다.
    }

    @Test
    void 잔액이_충분하다면_결제에_성공한다() {
        // given
        Point point = pointService.getPoint(USER_ID);
        pointService.chargePoint(USER_ID, 10_000L);

        // when
        Payment payment = paymentFacade.processPayment("userId" + USER_ID, token, reservation.id(), USER_ID);

        // then
        assertThat(payment).isNotNull();
        assertThat(payment.userId()).isEqualTo(USER_ID);
        assertThat(payment.reservationId()).isEqualTo(reservation.id());

        Point userPoint = pointService.getPoint(USER_ID);
        Reservation updatedReservation = reservationRepository.findById(reservation.id());

        assertThat(userPoint.amount()).isEqualTo(0); // 잔액 차감 확인
        assertThat(updatedReservation.status()).isEqualTo(ReservationStatus.COMPLETED); // 예약이 완료 상태로 변경되었는지 검증
    }

    @Test
    void 잔액이_부족할_경우_PAYMENT_FAILED_AMOUNT_에러를_반환한다() {
        // when & then
        // 잔액을 충전하지 않을 경우 잔액은 0이기 때문에 결제에 실패한다.
        assertThatThrownBy(() -> paymentFacade.processPayment("userId" + USER_ID, token, reservation.id(), USER_ID))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.PAYMENT_FAILED_AMOUNT.getMessage());
    }

    @Test
    void 예약자와_결제자_정보가_상이할_경우_PAYMENT_DIFFERENT_USER_에러를_반환한다() {
        // when & then
        assertThatThrownBy(() -> paymentFacade.processPayment("userId" + USER_ID, token, reservation.id(), 2L)) // 예약자 ID: 1L, 결제자 ID: 2L
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.PAYMENT_DIFFERENT_USER.getMessage());
    }

    @Test
    void 예약한지_5분이_지났을_경우_PAYMENT_TIMEOUT_에러를_반환한다() {
        Reservation timeHasPassedReservation = Reservation.builder()
                .id(2L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(USER_ID)
                .status(ReservationStatus.PAYMENT_WAITING)
                .reservationAt(LocalDateTime.now().minusMinutes(6)) // 5분 전 예약건으로 생성
                .build();
        reservationRepository.save(timeHasPassedReservation);
        // when & then
        assertThatThrownBy(() -> paymentFacade.processPayment("userId" + USER_ID, token, timeHasPassedReservation.id(), USER_ID))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.PAYMENT_TIMEOUT.getMessage());
    }

    @Test
    void 이미_결제된_예약건이라면_ALREADY_PAID_에러를_반환한다() {
        Reservation alreadyReserved = Reservation.builder()
                .id(2L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(USER_ID)
                .status(ReservationStatus.COMPLETED)
                .reservationAt(LocalDateTime.now().minusMinutes(6))
                .build();
        reservationRepository.save(alreadyReserved);
        // when & then
        assertThatThrownBy(() -> paymentFacade.processPayment("userId" + USER_ID, token, alreadyReserved.id(), USER_ID))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.ALREADY_PAID.getMessage());
    }

    @Test
    void 사용자가_동시에_여러_번_결제를_요청하면_한_번만_성공한다() throws InterruptedException {
        // given
        pointService.chargePoint(USER_ID, 100_000L);

        // when
        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        final int threadCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    paymentFacade.processPayment("userId" + USER_ID, token, reservation.id(), USER_ID);
                    successCnt.incrementAndGet();
                } catch(Exception e) {
                    logger.warn(e.getMessage());
                    failCnt.incrementAndGet();
                }
                finally {
                    countDownLatch.countDown();
                }
            }).start();
        }
        countDownLatch.await();

        Thread.sleep(1000);

        // 결제 요청이 한 번만 수행됐는지 검증한다.
        assertThat(successCnt.intValue()).isOne();
        // 실패한 횟수가 threadCount 에서 성공한 횟수를 뺀 값과 같은지 검증한다.
        assertThat(failCnt.intValue()).isEqualTo(threadCount - successCnt.intValue());
        // 결제 후 잔액이 충전금액 - 사용금액 인지 검증한다.
        Point point = pointService.getPoint(USER_ID);
        assertThat(point.amount()).isEqualTo(100_000L - 10_000L);
    }
}
