package io.concert.infra.entity;

import io.concert.domain.model.Schedule;
import io.concert.infra.enums.ConcertStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "schedule")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "schedule_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private ConcertEntity concert;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ConcertStatus status;

    private LocalDateTime concertAt;        // 콘서트 날짜
    private LocalDateTime reservationAt;    // 예약 시작 시간
    private LocalDateTime deadlineAt;       // 예약 마감 시간

    @OneToMany(mappedBy = "schedule")
    private List<ReservationEntity> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "schedule")
    private List<SeatEntity> seats = new ArrayList<>();

    public Schedule toDomain() {
        return new Schedule(
                id,
                concert.getId(),
                status,
                concertAt,
                reservationAt,
                deadlineAt
        );
    }

    public static ScheduleEntity from(Schedule schedule) {
        return ScheduleEntity.builder()
                .id(schedule.id())
                .concert(ConcertEntity.builder().id(schedule.id()).build())
                .status(schedule.status())
                .concertAt(schedule.concertAt())
                .reservationAt(schedule.reservationAt())
                .deadlineAt(schedule.deadlineAt())
                .build();
    }

}








