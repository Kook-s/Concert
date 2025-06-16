package io.concert.interfaces.dto;

import java.time.LocalDateTime;

public class AvailableConcertDto {
    long id;
    LocalDateTime concertAt;
    LocalDateTime reservationAt;
    LocalDateTime deadlineAt;
    String title;
    String description;
}
