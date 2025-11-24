package io.concert.application.facade;

import io.concert.domain.model.Point;
import io.concert.domain.service.PointService;
import io.concert.infra.repository.jpa.PointJpaRepository;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class PointFacadeIntegrationTest {

    Logger logger = LoggerFactory.getLogger(PointFacadeIntegrationTest.class);

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private PointService balanceService;

    @Autowired
    private PointJpaRepository balanceJpaRepository;

    private final Long USER_ID = 1L;

    @Test
    @Transactional
    void 유저의_잔액을_충전한다() {
        // given
        Long chargeAmount = 500L;

        // when
        Point updatedPoint = pointFacade.chargePoint("userId" + USER_ID, USER_ID, chargeAmount);

        // then
        assertThat(updatedPoint.amount()).isEqualTo(500L);
        assertThat(updatedPoint.userId()).isEqualTo(USER_ID);

        Point fetchedPoint = balanceService.getPoint(USER_ID);
        assertThat(fetchedPoint.amount()).isEqualTo(500L);
    }

    @Test
    @Transactional
    void 유저의_잔액을_조회한다() {
        // when
        Point fetchedPoint = pointFacade.getPoint(USER_ID);

        // then
        assertThat(fetchedPoint.amount()).isEqualTo(0L); // 초기 유저의 잔액은 0이다
        assertThat(fetchedPoint.userId()).isEqualTo(USER_ID);
    }

//    @Test
    @Transactional
    void 낙관적락_사용자가_동시에_여러_번_충전을_요청하면_모두_성공한다() throws InterruptedException {
        // given
        long chargeAmount = 100L;

        // when
        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        final int threadCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    pointFacade.chargePoint("userId" + USER_ID, USER_ID, chargeAmount);
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

        Point point = pointFacade.getPoint(USER_ID);

        // 충전 성공 횟수가 한 번인지 검증한다.
        assertThat(successCnt.intValue()).isOne();
        // 충전된 금액의 정합성이 보장되는지 검증한다.
        assertThat(chargeAmount * successCnt.intValue()).isEqualTo(point.amount());
    }

    @Test
    @Transactional
    void 분산락_사용자가_동시에_여러_번_충전을_요청하면_모두_성공한다() throws InterruptedException {
        // given
        long chargeAmount = 100L;

        // when
        AtomicInteger successCnt = new AtomicInteger(0);
        AtomicInteger failCnt = new AtomicInteger(0);

        final int threadCount = 100;
        final CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for(int i = 0; i < threadCount; i++) {
            new Thread(() -> {
                try {
                    pointFacade.chargePoint("userId" + USER_ID, USER_ID, chargeAmount);
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

        Point point = pointFacade.getPoint(USER_ID);

        // 충전 요청 성공 횟수가 스레드 갯수와 같은지 검증한다.
        assertThat(threadCount).isEqualTo(successCnt.intValue());
        // 충전된 금액의 정합성이 보장되는지 검증한다.
        assertThat(chargeAmount * successCnt.intValue()).isEqualTo(point.amount());
    }
}
