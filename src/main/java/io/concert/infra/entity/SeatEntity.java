package io.concert.infra.entity;

import io.concert.domain.model.Seat;
import io.concert.infra.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "seat")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id")
    private Long id;
    private String seatNo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SeatStatus status;

    private int seatPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_id")
    private ReservationEntity reservation;


    public Seat toDomain() {
        return new Seat(
                id,
                seatNo,
                status,
                seatPrice,
                schedule.getId(),
                reservation.getId()
        );
    }

    public static SeatEntity from(Seat seat) {
        return SeatEntity.builder()
                .id(seat.id())
                .seatNo(seat.seatNo())
                .seatPrice(seat.seatPrice())
                .schedule(ScheduleEntity.builder().id(seat.scheduleId()).build())
                .reservation(null)
                .build();
    }

}
