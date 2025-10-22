package io.concert.domain.service;

import io.concert.domain.model.Point;
import io.concert.domain.repository.PointRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PointServiceTest {

    @Mock
    private PointRepository pointRepository;

    @InjectMocks
    private PointService pointService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void User_Point_Select() {
        //given
        Long userId = 1L;
        Point point = Point.builder()
                .id(1L)
                .userId(userId)
                .amount(1000L)
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        when(pointRepository.findPoint(userId)).thenReturn(point);

        //when
        Point findPoint = pointService.getPoint(userId);

        //then
        assertThat(findPoint).isEqualTo(point);
        verify(pointRepository, times(1)).findPoint(userId);
    }

    @Test
    public void User_Point_Charge() {
        //given
        Long userId = 1L;
        Long chargePoint = 500L;

        Point point = Point.builder()
                .id(1L)
                .userId(userId)
                .amount(1000L)
                .lastUpdatedAt(LocalDateTime.now())
                .build();

        Point updatePoint = point.chargePoint(chargePoint);

        when(pointRepository.findPoint(userId)).thenReturn(point);
        pointRepository.save(point);

        //when
        Point result = pointService.chargePoint(userId, chargePoint);

        //then
        assertThat(result).usingRecursiveComparison()
                .ignoringFields("lastUpdatedAt")
                .isEqualTo(updatePoint);
        verify(pointRepository, times(1)).findPoint(userId);
        verify(pointRepository, times(1)).save(result);
    }
}
