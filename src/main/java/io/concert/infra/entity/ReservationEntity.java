package io.concert.infra.entity;

import io.concert.domain.model.Reservation;
import io.concert.infra.enums.ReservationStatus;
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
@Table(name = "reservation")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reservation_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "schedule_id")
    private ScheduleEntity schedule;

    @OneToMany(mappedBy = "reservation")
    private List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "reservation")
    private List<SeatEntity> seats = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private LocalDateTime reservationAt;

    public Reservation toDomain() {
        return new Reservation(
                id,
                user.getId(),
                schedule.getId(),
                status,
                reservationAt
        );
    }

    public static ReservationEntity from(Reservation reservation) {
        return ReservationEntity.builder()
                .id(reservation.id())
                .user(UserEntity.builder().id(reservation.id()).build())
                .schedule(ScheduleEntity.builder().id(reservation.id()).build())
                .status(reservation.status())
                .reservationAt(reservation.reservationAt())
                .build();
    }


}
