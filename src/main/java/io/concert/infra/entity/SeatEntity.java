package io.concert.infra.entity;

import io.concert.domain.model.Seat;
import io.concert.support.type.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "seat")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "concert_schedule_id", nullable = false)
    private ConcertScheduleEntity concertSchedule;

    @Column(nullable = false)
    private int seatNo;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatStatus status;

    private LocalDateTime reservationAt;

    @Column(nullable = false)
    private int seatPrice;

    public Seat of() {
        return Seat.builder()
                .id(id)
                .concertScheduleId(concertSchedule.getId())
                .seatNo(seatNo)
                .status(status)
                .reservationAt(reservationAt)
                .seatPrice(seatPrice)
                .build();
    }

    public static SeatEntity from(Seat seat) {
        return SeatEntity.builder()
                .id(seat.id())
                .concertSchedule(ConcertScheduleEntity.builder().id(seat.concertScheduleId()).build())
                .seatNo(seat.seatNo())
                .status(seat.status())
                .reservationAt(seat.reservationAt())
                .seatPrice(seat.seatPrice())
                .build();
    }
}
