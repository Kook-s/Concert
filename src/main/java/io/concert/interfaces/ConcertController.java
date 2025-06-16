package io.concert.interfaces;

import io.concert.application.ConcertFacade;
import io.concert.domain.model.Concert;
import io.concert.interfaces.dto.GetConcertDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/concert")
@RequiredArgsConstructor
public class ConcertController {

    private final ConcertFacade concertFacade;

    @GetMapping
    public ResponseEntity<GetConcertDto.ConcertResponse> getConcert() {

        List<Concert> concerts = concertFacade.getConcerts();

        return ResponseEntity.ok(GetConcertDto.ConcertResponse.of(concerts));
    }
}
