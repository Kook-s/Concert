package io.concert.interfaces.dto;

import io.concert.domain.model.Queue;
import io.concert.support.type.QueueStatus;
import lombok.Builder;

public class QueueDto {

    public record QueueRequest(
            Long userId
    ) {
    }

    @Builder
    public record QueueResponse(
            String token,
            QueueStatus status,
            Long rank
    ) {

        public static QueueResponse from(Queue token) {
            return QueueResponse.builder()
                    .token(token.token())
                    .status(token.status())
                    .rank(token.rank())
                    .build();
        }

    }
}
