package io.concert.application.facade;

import io.concert.application.dto.SeatResult;
import io.concert.domain.model.Concert;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ConcertRepository;
import io.concert.support.type.SeatStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
class ConcertFacadeIntegrationTest {

    @Autowired
    private ConcertFacade concertFacade;

    @Autowired
    private ConcertRepository concertRepository;

    @Test
    void 토큰이_유효하면_전체_콘서트를_조회할_수_있다() {
        // when
        List<Concert> concerts = concertFacade.getConcerts();

        // then
        assertDoesNotThrow(() -> concertFacade.getConcerts()); // 에러가 발생하지 않음을 검증
        assertThat(concerts).hasSize(2); // Test DB에 미리 넣어놓은 데이터
    }

    @Test
    void 예약_가능한_콘서트_스케줄을_조회한다() {
        // given
        List<Concert> concerts = concertRepository.findConcerts(); // Test DB에 넣어둔 concert 정보를 가져온다.
        Concert concert = concerts.get(0);

        // when
        List<ConcertSchedule> schedules = concertFacade.getConcertSchedules(concert.id());

        // then
        LocalDateTime now = LocalDateTime.now();
        for (ConcertSchedule schedule : schedules) {
            assertThat(schedule.concertId()).isEqualTo(concert.id()); // 조회한 일정이 해당 콘서트에 대한 일정인지 검증
            assertThat(schedule.reservationAt()).isBefore(now); // 예약 시간이 현재보다 이전인지 검증
            assertThat(schedule.deadline()).isAfter(now); // 예약 마감 시간이 현재보다 이후인지 검증
        }
    }

    @Test
    void 예약_가능한_좌석을_조회한다() {
        // given
        List<Concert> concerts = concertRepository.findConcerts(); // Test DB에 넣어둔 concert 정보를 가져온다.
        Concert concert = concerts.get(0);
        List<ConcertSchedule> concertSchedules = concertRepository.findConcertSchedules(concert.id());
        ConcertSchedule schedule = concertSchedules.get(0);

        // when
        SeatResult response = concertFacade.getSeats(concert.id(), schedule.id());

        // then
        assertThat(response.seats().get(0).status()).isEqualTo(SeatStatus.AVAILABLE);
        for (Seat seat : response.seats()) {
            assertThat(seat.concertScheduleId()).isEqualTo(schedule.id()); // 조회한 좌석이 해당 일정에 대한 좌석인지 검증
            assertThat(seat.status()).isEqualTo(SeatStatus.AVAILABLE); // 좌석 상태가 AVAILABLE 인지 검증
            assertThat(seat.reservationAt()).isNull(); // 예약되지 않았는지 검증
        }
    }
}
