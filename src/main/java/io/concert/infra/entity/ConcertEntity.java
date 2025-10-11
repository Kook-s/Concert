package io.concert.infra.entity;

import io.concert.support.type.ConcertStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "concert")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ConcertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private ConcertStatus status;
}
