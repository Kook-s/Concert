package io.concert.infra.entity;

import io.concert.domain.model.Concert;
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

    public Concert of(){
        return Concert.builder()
                .id(id)
                .title(title)
                .description(description)
                .status(status)
                .build();
    }

    public static ConcertEntity from(Concert concert){
        return ConcertEntity.builder()
                .id(concert.id())
                .title(concert.title())
                .description(concert.description())
                .status(concert.status())
                .build();
    }

}
