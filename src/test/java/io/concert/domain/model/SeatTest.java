package io.concert.domain.model;

import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.type.SeatStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class SeatTest {
    @Test
    void 좌석_상태가_사용_불가능하면_예외가_발생한다() {
        // given
        Seat unavailableSeat = Seat.builder()
                .id(1L)
                .concertScheduleId(1L)
                .seatNo(1)
                .status(SeatStatus.UNAVAILABLE)
                .reservationAt(LocalDateTime.now())
                .seatPrice(10000)
                .build();

        // when & then
        assertThatThrownBy(unavailableSeat::checkStatus)
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.SEAT_UNAVAILABLE.getMessage());
    }

    @Test
    void 좌석_할당_시_상태가_UNAVAILABLE로_변경된다() {
        // given
        Seat availableSeat = Seat.builder()
                .id(1L)
                .concertScheduleId(1L)
                .seatNo(1)
                .status(SeatStatus.AVAILABLE)
                .seatPrice(10000)
                .build();

        // when
        Seat assignedSeat = availableSeat.assign();

        // then
        assertThat(assignedSeat.status()).isEqualTo(SeatStatus.UNAVAILABLE);
        assertThat(assignedSeat.reservationAt()).isNotNull(); // 예약 시간이 설정되었는지 확인
        assertThat(assignedSeat.reservationAt()).isBefore(LocalDateTime.now().plusSeconds(1)); // 예약 시간이 현재보다 이전인지 확인
    }
}