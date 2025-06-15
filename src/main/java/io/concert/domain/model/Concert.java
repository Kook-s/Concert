package io.concert.domain.model;

import io.concert.infra.enums.ConcertStatus;

public record Concert(
        long id,
        String title,
        String description,
        ConcertStatus status
) {
}
