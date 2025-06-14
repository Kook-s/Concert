package io.concert.domain.service;

import io.concert.domain.model.User;
import io.concert.domain.repository.UserRepository;
import io.concert.support.CustomException;
import io.concert.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User getUser(long id) {
        User findUser = userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        return findUser;
    }
}
