package io.concert.interfaces.dto;

import io.concert.domain.model.Concert;
import lombok.Builder;

import java.util.List;

public class GetConcertDto {

    @Builder
    public record ConcertResponse(
            List<ConcertDto> concerts
    ){

        public static ConcertResponse of(List<Concert> concerts){
            List<ConcertDto> list = concerts.stream()
                    .map(concert -> ConcertDto.builder()
                            .id(concert.id())
                            .title(concert.title())
                            .description(concert.description())
                            .build()
                    )
                    .toList();

            return ConcertResponse.builder()
                    .concerts(list)
                    .build();

        }
    }
}
