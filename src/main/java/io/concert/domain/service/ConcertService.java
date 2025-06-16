package io.concert.domain.service;

import io.concert.domain.model.Concert;
import io.concert.domain.repository.ConcertRepository;
import io.concert.support.CustomException;
import io.concert.support.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConcertService {

    private final ConcertRepository concertRepository;

    public Concert getConcert(long id) {
        return concertRepository.findById(id)
                .orElseThrow(()-> new CustomException(ErrorCode.CONCERT_NOT_FOUND));
    }
}
