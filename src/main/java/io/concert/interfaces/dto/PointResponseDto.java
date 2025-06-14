package io.concert.interfaces.dto;

import io.concert.domain.model.Point;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PointResponse {
    long id;
    long userId;
    int  amount;
    LocalDateTime updatedAt;

    public PointResponse(long id, long userId, int amount, LocalDateTime updatedAt) {
        this.id = id;
        this.userId = userId;
        this.amount = amount;
        this.updatedAt = updatedAt;
    }

    public static PointResponse from(Point point) {
        return new PointResponse(
                point.id(),
                point.userId(),
                point.amount(),
                point.updatedAt()
        );
    }
}
