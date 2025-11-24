package io.concert.application.facade;

import io.concert.domain.model.Queue;
import io.concert.support.type.QueueStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD;

@SpringBootTest
@DirtiesContext(classMode = BEFORE_EACH_TEST_METHOD)
public class QueueFacadeIntegrationTest {

    @Autowired
    private QueueFacade queueFacade;

    private final Long USER_ID = 1L;

    @Test
    @Transactional
    void 토큰을_생성한다() {
        // given
        Long userId = USER_ID;

        // when
        Queue token = queueFacade.issueToken("queue", userId);

        // then
        assertThat(token).isNotNull();
        assertThat(token.userId()).isEqualTo(userId);
        assertThat(token.status()).isEqualTo(QueueStatus.ACTIVE);
    }

    @Test
    @Transactional
    void 토큰이_활성_상태인_사용자가_200명인_경우_이후_요청자는_대기_상태의_토큰을_받는다() {
        // given
        List<Queue> tokenList = new ArrayList<>();
        int activeCnt = 0;
        int waitingCnt = 0;

        // when
        // 200명의 대기자를 대기열에 추가
        for(long l = 1; l <= 201; l++) {
            Queue dummyToken = queueFacade.issueToken("queue", l);
            tokenList.add(dummyToken);
        }

        // then
        for (Queue queue : tokenList) {
            QueueStatus status = queue.status();
            if (status.equals(QueueStatus.ACTIVE)) {
                activeCnt++;
            } else if (status.equals(QueueStatus.WAITING)){
                waitingCnt++;
            }
        }

        // ACTIVE 토큰이 200개인지 검증
        assertThat(activeCnt + waitingCnt).isEqualTo(201);

        // WAITING 토큰이 1개인지 검증한다.
        assertThat(waitingCnt).isOne();
    }
}
