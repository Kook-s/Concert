package io.concert.support.code;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;

@Getter
@AllArgsConstructor
public enum ErrorType {
    HTTP_MESSAGE_NOT_READABLE(ErrorCode.TOKEN_ERROR, "유효하지 않은 타입이거나 요청 값이 누락되었습니다.", LogLevel.WARN);

    private final ErrorCode code;
    private final String message;
    private final LogLevel logLevel;

}
