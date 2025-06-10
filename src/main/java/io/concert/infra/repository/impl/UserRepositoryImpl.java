package io.concert.infra.repository.impl;

import io.concert.domain.model.User;
import io.concert.domain.repository.UserRepository;
import io.concert.infra.entity.UserEntity;
import io.concert.infra.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<User> findById(long id) {
        return userJpaRepository.findById(id).map(UserEntity::toUser);
    }
}
