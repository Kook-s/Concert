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

    public PointEntity(Long id, int amount, LocalDateTime updatedAt) {
        this.id = id;
        this.amount = amount;
        this.updatedAt = updatedAt;
    }

    public Point toPoint(){
        return new Point(id, user.getId(), amount, updatedAt);
    }

    public void increaseAmount(int amount){
        if(amount > 0){
            this.amount += amount;
        }else {
           throw new IllegalArgumentException("충전할 수 없는 금액입니다.");
        }
    }

//    public void decreaseAmount(int amount){
//        if(amount > 0){
//            this.amount -= amount;
//        }
//    }

}
