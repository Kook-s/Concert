package io.concert.interfaces.interceptor.controller;

import io.concert.application.facade.QueueFacade;
import io.concert.domain.model.Queue;
import io.concert.interfaces.dto.QueueDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/queue")
@RequiredArgsConstructor
public class QueueController {

    private final QueueFacade queueFacade;

    /**
     * 대기열 등록, 토큰 발급
     */
    @PostMapping("/tokens")
    public ResponseEntity<QueueDto.QueueResponse> issueToken(
            @Valid @RequestBody QueueDto.QueueRequest queueRequest
    ) {
        Queue token = queueFacade.issueToken("queue", queueRequest.userId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(QueueDto.QueueResponse.of(token));
    }

    /**
     * 대기열 상태 조회
     */
    @GetMapping("/status")
    public ResponseEntity<QueueDto.QueueResponse> status(
            @RequestHeader("Token") @NotBlank String token,
            @RequestHeader("User-Id") Long userId
    ) {
        Queue queue = queueFacade.status(token, userId);
        return ResponseEntity.ok(QueueDto.QueueResponse.of(queue));
    }
}
