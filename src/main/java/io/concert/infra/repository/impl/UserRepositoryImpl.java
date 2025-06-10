package io.concert.infra.repository.impl;

import io.concert.domain.model.User;
import io.concert.domain.repository.UserRepository;
import io.concert.infra.entity.UserEntity;
import io.concert.infra.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(long id) {
        Optional<UserEntity> entity = userJpaRepository.findById(id);
        System.out.println(">>> userJpaRepository result: " + entity);

        return userJpaRepository.findById(id).map(UserEntity::toUser);
    }
}
