package io.concert.infra.entity;

import io.concert.domain.model.Point;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "point")
@NoArgsConstructor
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "point_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    private int amount = 0;
    private LocalDateTime updatedAt;

    public Point toPoint(){
        return new Point(id, user.getId(), amount, updatedAt);
    }
}
