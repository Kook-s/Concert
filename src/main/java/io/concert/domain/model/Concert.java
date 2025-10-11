package io.concert.domain.model;

import io.concert.support.type.ConcertStatus;
import lombok.Builder;

@Builder
public record Concert(
        Long id,
        String title,
        String description,
        ConcertStatus status
) {

    public boolean checkStatus() {
        return status.equals(ConcertStatus.AVAILABLE);
    }
}
