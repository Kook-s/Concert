package io.concert.infra.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Entity(name = "users")
@Getter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    String username;
}
