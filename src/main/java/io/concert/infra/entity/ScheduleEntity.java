package io.concert.infra.entity;

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
    private LocalDateTime concertAt;        // 콘서트 날짜
    private LocalDateTime reservationAt;    // 예약 시작 시간
    private LocalDateTime deadlineAt;       // 예약 마감 시간

    @OneToMany(mappedBy = "schedule")
    private List<ReservationEntity> reservations = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "concert_id")
    private ConcertEntity concert;


}
