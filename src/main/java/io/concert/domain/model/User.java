package io.concert.domain.model;

public record User(
        Long id,
        String email,
        String password
) {
}
