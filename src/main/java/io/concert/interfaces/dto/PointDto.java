package io.concert.interfaces.dto;

import io.concert.domain.model.Point;
import jakarta.validation.constraints.Min;
import lombok.Builder;

public class PointDto {

    public record PointRequest(
            @Min(value = 1, message = "충전 금액은 1원 이상이어야 합니다.")
            Long amount
    ) {
    }

    @Builder
    public record PointResponse(
            Long userId,
            Long currentAmount
    ) {
        public static PointResponse of(Point point) {
            return PointResponse.builder()
                    .userId(point.userId())
                    .currentAmount(point.amount())
                    .build();
        }

    }
}
