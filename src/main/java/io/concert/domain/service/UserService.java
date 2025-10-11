package io.concert.domain.service;

import io.concert.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void validateUser(Long userId) {
        userRepository.existsUses(userId);
    }
}
