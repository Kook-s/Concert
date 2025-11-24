package io.concert.domain.service;

import io.concert.domain.model.Queue;
import io.concert.domain.repository.QueueRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QueueServiceTest {

    @Mock
    private QueueRepository queueRepository;

    @InjectMocks
    private QueueService queueService;

    private final Long USER_ID = 1L;
    private final String TOKEN = "test-token";

    @Test
    void 활성_토큰이_200_미만일_때_활성_토큰을_생성한다() {
        // given
        when(queueRepository.getActiveTokenCount()).thenReturn(150L); // 활성 토큰이 150개인 경우
        when(queueRepository.getWaitingTokenCount()).thenReturn(0L); // 대기 토큰이 없는 경우

        // When
        Queue result = queueService.issueToken(USER_ID);

        // Then
        verify(queueRepository).saveActiveToken(result.token()); // 활성 상태이므로 활성 토큰이 저장되어야 함
        verify(queueRepository, never()).saveWaitingToken(anyString()); // 대기열에 저장되지 않음
    }

    @Test
    void 활성_토큰이_200_이상일_때_대기열에_추가한다() {
        // given
        when(queueRepository.getActiveTokenCount()).thenReturn(200L); // 활성 토큰이 200개인 경우
        when(queueRepository.getWaitingTokenCount()).thenReturn(1L); // 대기 토큰이 있는 경우

        // When
        Queue result = queueService.issueToken(USER_ID);

        // Then
        verify(queueRepository).saveWaitingToken(result.token()); // 활성 상태이므로 활성 토큰이 저장되어야 함
        verify(queueRepository, never()).saveActiveToken(anyString()); // 대기열에 저장되지 않음
    }

    @Test
    void 활성_토큰을_삭제한다() {
        // given
        doNothing().when(queueRepository).removeToken(TOKEN);

        // when
        queueService.expireToken(TOKEN);

        // then
        verify(queueRepository).removeToken(TOKEN); // removeToken 메소드가 호출되었는지 확인

    }

    @Test
    void 활성_토큰이면_유효성_검증에_통과한다() {
        // given
        when(queueRepository.activeTokenExist(TOKEN)).thenReturn(true); // 활성 토큰이 존재하는 경우

        // when
        queueService.validateToken(TOKEN);

        // then
        verify(queueRepository).activeTokenExist(TOKEN); // activeTokenExist가 호출되었는지 확인
    }

    @Test
    void 토큰이_없거나_대기_상태이면_유효성_검증시_에러를_반환한다() {
        // given
        when(queueRepository.activeTokenExist(TOKEN)).thenReturn(false); // 활성 토큰이 존재하지 않는 경우

        // when & then
        assertThrows(CoreException.class, () -> queueService.validateToken(TOKEN));
        assertThatThrownBy(() -> queueService.validateToken(TOKEN))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.TOKEN_INVALID.getMessage());
    }

    @Test
    void 토큰을_조회한다() {
        // given
        Queue mockToken = mock(Queue.class);
        when(queueRepository.findToken(TOKEN)).thenReturn(mockToken); // 지정된 토큰을 조회

        // when
        Queue result = queueService.getToken(TOKEN);

        // then
        assertThat(result).isEqualTo(mockToken); // 반환된 값이 올바른지 확인
    }

    @Test
    void 활성_토큰이_200개_미만이면_대기열_토큰을_활성_상태로_변경한다() {
        // given
        long activeTokenCount = 190L; // 예를 들어 현재 활성 토큰이 190개라고 가정
        long neededTokens = 200L - activeTokenCount; // 필요 토큰 수는 10개
        List<Object> waitingTokens = Arrays.asList("waiting-token1", "waiting-token2");

        when(queueRepository.getActiveTokenCount()).thenReturn(activeTokenCount);
        when(queueRepository.getWaitingTokens(neededTokens)).thenReturn(waitingTokens);

        // when
        queueService.updateActiveTokens();

        // then
        verify(queueRepository).getActiveTokenCount();
        verify(queueRepository).getWaitingTokens(neededTokens);
        verify(queueRepository).removeWaitingToken(new HashSet<>(waitingTokens));

        // 개별 토큰이 saveActiveToken으로 저장되었는지 확인
        waitingTokens.forEach(token -> verify(queueRepository).saveActiveToken(token));
    }

    @Test
    void 활성_토큰이_200개_이상이면_대기열_토큰의_상태를_변경하지_않는다() {
        // given
        when(queueRepository.getActiveTokenCount()).thenReturn(200L); // 현재 활성 토큰이 200개

        // when
        queueService.updateActiveTokens();

        // then
        verify(queueRepository, never()).getWaitingTokens(anyLong()); // 대기열에서 토큰을 가져오는 동작이 발생하지 않음

    }
}