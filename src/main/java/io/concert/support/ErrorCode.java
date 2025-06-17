package io.concert.support;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 사용자를 조회할 수 없습니다."),
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포인트를 조회할 수 없습니다."),
    SEAT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 좌석은 조회할 수 없습니다."),
    CONCERT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 콘서트를 조회할 수 없습니다."),
    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "예약 내역을 조회할 수 없습니다."),
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일정을 조회할 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
