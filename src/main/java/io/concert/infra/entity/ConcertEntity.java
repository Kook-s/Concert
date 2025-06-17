package io.concert.infra.entity;

import io.concert.domain.model.Concert;
import io.concert.infra.enums.ConcertStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "concert")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "concert_id")
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "concert", fetch = FetchType.LAZY)
    private List<ScheduleEntity> schedules = new ArrayList<>();

    public Concert toDomain() {
        return new Concert(
                id,
                title,
                description
        );
    }

    public static ConcertEntity from (Concert concert) {
        return ConcertEntity.builder()
                .id(concert.id())
                .title(concert.title())
                .description(concert.description())
                .build();
    }

}
