package io.concert.interfaces.dto;

import lombok.Builder;

@Builder
public record ConcertDto(
        long id,
        String title,
        String description
) {
}
