package io.concert.infra.entity;

import io.concert.infra.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "seat")
@NoArgsConstructor
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;
    private String seatNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;
    private LocalDateTime reservationAt;
    private int seatPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;
}
