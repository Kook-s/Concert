package io.concert.infra.repository.impl;

import io.concert.domain.repository.UserRepository;
import io.concert.infra.repository.jpa.UserJpaRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public void existsUses(Long userId) {
        if(!userJpaRepository.existsById(userId)) {
            throw new CoreException(ErrorType.RESOURCE_NOT_FOUND, "사용자 ID: " + userId);
        }
    }
}
