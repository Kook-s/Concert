package io.concert.support.exception;

import io.concert.support.code.ErrorType;
import lombok.Builder;

import java.util.Map;

@Builder
public record ErrorResponse(
        String errorCode,
        String message,
        Object payload
) {

    public static ErrorResponse of(CoreException e){
        return ErrorResponse.builder()
                        .errorCode(e.getErrorType().getCode().name())
                        .message(e.getErrorType().getMessage())
                        .payload(e.getPayload())
                        .build();
    }

    public static ErrorResponse of(ErrorType errorType, Map<String, String> errors) {
        return ErrorResponse.builder()
                .errorCode(errorType.getCode().name())
                .message(errorType.getMessage())
                .payload(errors)
                .build();
    }

    public static ErrorResponse of(ErrorType errorType, String message) {
        return ErrorResponse.builder()
                .errorCode(errorType.getCode().name())
                .message(errorType.getMessage())
                .payload(message)
                .build();
    }
}
