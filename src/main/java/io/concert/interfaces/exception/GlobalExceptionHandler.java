package io.concert.interfaces.exception;

import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 비즈니스 로직 수행이 불가능한 경우
    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorResponse> handleCoreException(CoreException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("Business ERROR Occurred: {}, {}", e.getMessage(), e.getPayload(), e);
            case WARN -> log.warn("Business WARN Occurred: {}, {}", e.getMessage(), e.getPayload());
            default -> log.info("Business INFO Occurred: {}, {}", e.getMessage(), e.getPayload());
        }

        HttpStatus status;
        switch (e.getErrorType().getCode()) {
            case DB_ERROR -> status = HttpStatus.INTERNAL_SERVER_ERROR;
            case CLIENT_ERROR -> status = HttpStatus.BAD_REQUEST;
            case TOKEN_ERROR -> status = HttpStatus.UNAUTHORIZED;
            default -> status = HttpStatus.OK;
        }
        return new ResponseEntity<>(ErrorResponse.of(e), status);
    }

    // API 호출 시 '객체' 혹은 '파라미터' 데이터 값이 유효하지 않은 경우
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        log.error("Method Argument Not Valid: {}, message: {}", ex.getMessage(), errors);
        return new ResponseEntity<>(ErrorResponse.of(ErrorType.CLIENT_ERROR, errors), HttpStatus.BAD_REQUEST);
    }

    // 헤더 값이 누락된 경우
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handle(HttpMessageNotReadableException ex) {
        log.error("Unable to read HTTP message: {}", ex.getMessage());
        return new ResponseEntity<>(ErrorResponse.of(ErrorType.MISSING_TOKEN, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    // 모든 Exception 경우 발생
    @ExceptionHandler(Exception.class)
    protected final ResponseEntity<ErrorResponse> handleAllExceptions(Exception ex) {
        log.error("Server Error Occurred: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ErrorResponse.of(ErrorType.INTERNAL_ERROR, ex.getMessage()), HttpStatus.OK);
    }

}
