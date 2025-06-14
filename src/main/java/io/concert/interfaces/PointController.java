package io.concert.interfaces;

import io.concert.domain.model.Point;
import io.concert.domain.model.User;
import io.concert.domain.service.PointService;
import io.concert.domain.service.UserService;
import io.concert.interfaces.dto.PointResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/point")
@RequiredArgsConstructor
public class PointController {

    private final PointService pointService;
    private final UserService userService;


    @GetMapping
    public ResponseEntity<PointResponse> getPoint(@RequestParam long userId) {
        User findUser = userService.getUser(userId);
        Point findPoint = pointService.getPoint(findUser.id());

        return ResponseEntity.ok(PointResponse.from(findPoint));
    }

    @PostMapping
    public ResponseEntity<String> chargePoint(
            @RequestParam long userId,
            @RequestParam int amount
    ) {
        User findUser = userService.getUser(userId);
        pointService.chargePoint(findUser.id(), amount);

        return ResponseEntity.ok("Success");
    }

}
