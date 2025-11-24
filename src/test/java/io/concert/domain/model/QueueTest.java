package io.concert.domain.model;

import io.concert.support.type.QueueStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class QueueTest {

    @Test
    public void 대기순번0_활성토큰_200개_미안인_경우_ACTIVATE_상태_토큰_생성() {
        //given
        Long userId = 1L;
        Long activateCount = 30L;
        Long rank = 0L;

        //when
        Queue token = Queue.createToken(userId, activateCount, rank);

        //then
        assertThat(token.status()).isEqualTo(QueueStatus.ACTIVE);
        assertThat(token.rank()).isEqualTo(0L);
        assertThat(token.expiredAt()).isNotNull();
    }

    @Test
    public void 대기순번1_이상인_경우_WAITING_상태_토큰_생성() {
        //given
        Long userId = 1L;
        Long activateCount = 50L;
        Long rank = 1L;

        //when
        Queue token = Queue.createToken(userId, activateCount, rank);

        //then
        assertThat(token.status()).isEqualTo(QueueStatus.WAITING);
        assertThat(token.rank()).isEqualTo(2L);
        assertThat(token.expiredAt()).isNull();
    }

    @Test
    public void 토큰_만료_상태_변경() {
        //given
        Queue token = Queue.builder()
                .status(QueueStatus.ACTIVE)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();

        //when
        Queue expiredToken = token.expired();

        //then
        assertThat(expiredToken.status()).isEqualTo(QueueStatus.EXPIRED);
        assertThat(expiredToken.expiredAt()).isBeforeOrEqualTo(LocalDateTime.now());
    }

    @Test
    public void 토큰_상태_체크_ACTIVATE() {
        //given
        Queue token = Queue.builder()
                .status(QueueStatus.ACTIVE)
                .build();
        //when
        boolean isActivate = token.checkStatus();

        //then
        assertThat(isActivate).isTrue();
    }

    @Test
    public void 유효성_검사_시_활설_상태_성공() {
        //given
        Queue validToken = Queue.builder()
                .status(QueueStatus.ACTIVE)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
        //when
        //then
        validToken.validateToken();
    }
}
