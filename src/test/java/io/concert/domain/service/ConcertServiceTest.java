package io.concert.domain.service;

import io.concert.domain.model.Concert;
import io.concert.domain.model.ConcertSchedule;
import io.concert.domain.model.Seat;
import io.concert.domain.repository.ConcertRepository;
import io.concert.support.code.ErrorType;
import io.concert.support.exception.CoreException;
import io.concert.support.type.ConcertStatus;
import io.concert.support.type.SeatStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ConcertServiceTest {

    @Mock
    private ConcertRepository concertRepository;

    @InjectMocks
    private ConcertService concertService;

    private Concert concert;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        concert = Concert.builder()
                .id(1L)
                .title("Test Title")
                .description("Test Description")
                .status(ConcertStatus.AVAILABLE)
                .build();
    }

    @Test
    public void All_Concert_Select() {
        //given
        when(concertRepository.findConcerts()).thenReturn(Arrays.asList(concert));

        //when
        List<Concert> concerts = concertService.getConcerts();

        //then
        assertThat(concerts).hasSize(1);
        assertThat(concerts.get(0).id()).isEqualTo(concert.id());
    }

    @Test
    public void Active_Concert_Schedule_Select() {
        //given
        ConcertSchedule schedule = ConcertSchedule.builder()
                .id(1L)
                .concertId(concert.id())
                .reservationAt(LocalDateTime.now().minusDays(1))
                .deadline(LocalDateTime.now().plusDays(1))
                .concertAt(LocalDateTime.now().plusDays(5))
                .build();

        when(concertRepository.findConcert(concert.id())).thenReturn(concert);
        when(concertRepository.findConcertSchedules(concert.id())).thenReturn(Arrays.asList(schedule));

        //when
        List<ConcertSchedule> schedules = concertService.getConcertSchedules(concert);

        //then
        assertThat(schedules).hasSize(1);
        assertThat(schedules.get(0).concertId()).isEqualTo(concert.id());
    }

    @Test
    public void Available_Seat_Select() {
        //given
        Seat seat = Seat.builder()
                .id(1L)
                .concertScheduleId(1L)
                .seatNo(1)
                .seatPrice(1000)
                .status(SeatStatus.AVAILABLE)
                .build();

        when(concertRepository.findSeats(concert.id(), 1L, SeatStatus.AVAILABLE)).thenReturn(Arrays.asList(seat));

        //when
        List<Seat> seats = concertService.getSeats(concert.id(), 1L, SeatStatus.AVAILABLE);

        //then
        assertThat(seats).hasSize(1);
        assertThat(seats.get(0).concertScheduleId()).isEqualTo(seat.concertScheduleId());
    }

    @Test
    public void Unavailable_Reservation_Exception() {
        //given
        ConcertSchedule schedule = mock(ConcertSchedule.class);
        Seat unavailableSeat = Seat.builder()
                .id(1L)
                .concertScheduleId(1L)
                .seatNo(1)
                .seatPrice(1000)
                .status(SeatStatus.UNAVAILABLE)
                .build();
        //when
        //then
        assertThatThrownBy(() -> concertService.validateReservationAvailability(schedule, unavailableSeat))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.SEAT_UNAVAILABLE.getMessage());
    }

    @Test
    public void Unavailable_ReservationAt_Exception() {
        //given
        ConcertSchedule schedule = ConcertSchedule.builder()
                .id(1L)
                .concertId(1L)
                .reservationAt(LocalDateTime.now().minusDays(1))
                .deadline(LocalDateTime.now().minusDays(1))
                .concertAt(LocalDateTime.now().plusDays(1))
                .build();

        Seat seat = mock(Seat.class);

        //when
        //then
        assertThatThrownBy(() -> concertService.validateReservationAvailability(schedule, seat))
                .isInstanceOf(CoreException.class)
                .hasMessageContaining(ErrorType.AFTER_DEADLINE.getMessage());
    }
}



















