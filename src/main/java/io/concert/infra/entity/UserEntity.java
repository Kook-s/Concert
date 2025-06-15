package io.concert.infra.entity;

import io.concert.domain.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String email;
    private String password;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private PointEntity point;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<ReservationEntity> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PaymentEntity> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<QueueEntity> queues = new ArrayList<>();

    public UserEntity(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public User toUser() {
        return new User(id, email, password);
    }

    public static UserEntity from(User user) {
        return new UserEntity(
                user.id(),
                user.email(),
                user.password()
                );
    }
}
