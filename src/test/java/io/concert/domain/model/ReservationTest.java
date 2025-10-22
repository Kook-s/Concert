package io.concert.domain.model;

import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.type.ReservationStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ReservationTest {

    @Test
    void 같은_예약건으로_결제되지_않았고_5분_안에_동일한_사용자가_결제하려고_하면_성공한다() {
        // given
        Reservation reservation = Reservation.builder()
                .id(1L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PAYMENT_WAITING)
                .reservationAt(LocalDateTime.now())  // 현재 시간으로 예약
                .build();

        // when & then
        reservation.checkValidation(1L); // 동일한 유저로 검증
    }

    @Test
    void 예약이_이미_결제_완료_상태라면_ALREADY_PAID_에러를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .id(1L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.COMPLETED)
                .reservationAt(LocalDateTime.now().minusMinutes(6))  // 6분 전에 예약
                .build();

        // when & then
        assertThatThrownBy(() -> reservation.checkValidation(1L))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.ALREADY_PAID.getMessage());
    }

    @Test
    void 예약_시간이_5분_초과되면_PAYMENT_TIMEOUT_에러를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .id(1L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PAYMENT_WAITING)
                .reservationAt(LocalDateTime.now().minusMinutes(6))  // 6분 전에 예약
                .build();

        // when & then
        assertThatThrownBy(() -> reservation.checkValidation(1L))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.PAYMENT_TIMEOUT.getMessage());
    }

    @Test
    void 예약자_정보가_결제하려는_사용자의_ID와_상이하면_PAYMENT_DIFFERENT_USER_에러를_반환한다() {
        // given
        Reservation reservation = Reservation.builder()
                .id(1L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PAYMENT_WAITING)
                .reservationAt(LocalDateTime.now())  // 현재 시간으로 예약
                .build();

        // when & then
        assertThatThrownBy(() -> reservation.checkValidation(2L))  // 다른 유저 ID
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.PAYMENT_DIFFERENT_USER.getMessage());
    }

    @Test
    void 예약_상태를_COMPLETED로_변경한다() {
        // given
        Reservation reservation = Reservation.builder()
                .id(1L)
                .concertId(1L)
                .scheduleId(1L)
                .seatId(1L)
                .userId(1L)
                .status(ReservationStatus.PAYMENT_WAITING)
                .reservationAt(LocalDateTime.now())
                .build();

        // when
        Reservation updatedReservation = reservation.changeStatus(ReservationStatus.COMPLETED);

        // then
        assertThat(updatedReservation.status()).isEqualTo(ReservationStatus.COMPLETED);
        assertThat(updatedReservation.id()).isEqualTo(reservation.id());
        assertThat(updatedReservation.concertId()).isEqualTo(reservation.concertId());
    }

}