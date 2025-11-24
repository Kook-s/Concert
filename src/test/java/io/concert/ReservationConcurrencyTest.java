package io.concert;

import io.concert.application.dto.ReservationCommand;
import io.concert.application.dto.ReservationResult;
import io.concert.application.facade.ReservationFacade;
import io.concert.domain.repository.ReservationRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class ReservationConcurrencyTest {

    @Autowired
    private ReservationFacade reservationFacade;

    @Autowired
    private ReservationRepository reservationRepository;

    @DisplayName("동시에 같은 좌석 예약 요청 시, 한 명만 성공")
    @Test
    public void 동시에_같은좌석_예약시_한명만_성공 () throws InterruptedException {
        //given
        Long concertId = 1L;
        Long concertScheduleId = 1L;
        Long seatId = 1L;

        int threadCount = 30;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        List<ReservationResult> successResults = Collections.synchronizedList(new ArrayList<>());

        //when
        for (int i = 0; i < threadCount; i++) {
            long userId = i;
            executorService.submit(() -> {
                try {
                    ReservationCommand command = ReservationCommand.builder()
                            .userId(userId)
                            .concertId(concertId)
                            .scheduleId(concertScheduleId)
                            .seatId(seatId)
                            .build();

                    ReservationResult result = reservationFacade.reservation("RESERVATION:" + seatId, command);
                    successResults.add(result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        executorService.shutdown();

        //then
        assertThat(successResults.size()).as("같은 좌석에 대해서는 한명만 예약 성공해야 함").isEqualTo(1);
    }
}
