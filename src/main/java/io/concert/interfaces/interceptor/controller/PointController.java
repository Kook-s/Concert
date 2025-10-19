package io.concert.interfaces.interceptor.controller;

import io.concert.application.facade.PointFacade;
import io.concert.domain.model.Point;
import io.concert.interfaces.dto.PointDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class PointController {

    private final PointFacade pointFacade;

    /**
     * 잔액 조회
     */
    @GetMapping("/users/{userId}/point")
    public ResponseEntity<PointDto.PointResponse> getPoint(
            @PathVariable Long userId
    ) {
        Point point = pointFacade.getPoint(userId);
        return ResponseEntity.ok(PointDto.PointResponse.of(point));
    }

    /**
     * 잔액 충전
     */
    @PostMapping("/users/{userId}/point")
    public ResponseEntity<PointDto.PointResponse> chargePoint(
            @PathVariable Long userId,
            @Valid @RequestBody PointDto.PointRequest request
    ) {
        Point point = pointFacade.chargePoint("userId:" + userId, userId, request.amount());
        return ResponseEntity.ok(PointDto.PointResponse.of(point));

    }
}
