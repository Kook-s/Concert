package io.concert.domain.repository;


import io.concert.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findById(long id);
}
