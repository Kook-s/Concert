package io.concert.domain.model;

import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PointTest {

    @Test
    public void 사용_금액이_같거나_많으면_성공() {
        //given
        Point point = Point.builder()
                .id(1L)
                .userId(1L)
                .amount(10_000L)
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        //when
        Point updatePoint = point.usePoint(5_000);

        //then
        assertThat(updatePoint.amount()).isEqualTo(5_000);
        assertThat(updatePoint.lastUpdatedAt()).isAfter(point.lastUpdatedAt());
    }

    @Test
    public void 부족하면_실패(){
        //given
        Point point = Point.builder()
                .id(1L)
                .userId(1L)
                .amount(3_000L)
                .lastUpdatedAt(LocalDateTime.now())
                .build();
        //when
        //then
        assertThatThrownBy(() -> point.usePoint(5_000))
                .isInstanceOf(CoreException.class)
                .hasMessage(ErrorType.PAYMENT_FAILED_AMOUNT.getMessage());

    }
}
