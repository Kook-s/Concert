package io.concert.infra.entity;

import io.concert.infra.enums.ConcertStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "concert")
@NoArgsConstructor
public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    private String title;
    private String description;
    private ConcertStatus status;

    @OneToMany(mappedBy = "concert", fetch = FetchType.LAZY)
    private List<ScheduleEntity> schedules = new ArrayList<>();
}
