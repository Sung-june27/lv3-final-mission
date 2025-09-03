package finalmission.service;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.global.error.exception.BadRequestException;
import finalmission.reservation.repository.ReservationRepository;
import finalmission.room.repository.ConferenceRoomRepository;
import finalmission.room.service.ConferenceRoomService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ConferenceRoomServiceTest {

    @InjectMocks
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private ConferenceRoomRepository conferenceRoomRepository;
    @Mock
    private ReservationRepository reservationRepository;

    @DisplayName("회의실을 삭제할 수 있다.")
    @Test
    void deleteById() {
        // given
        when(reservationRepository.existsByConferenceRoomId(anyLong()))
                .thenReturn(false);

        // when
        conferenceRoomService.deleteById(1L);

        // then
        verify(conferenceRoomRepository).deleteById(1L);
    }

    @DisplayName("예약이 있는 회의실은 삭제할 수 없다.")
    @Test
    void deleteById_WhenReserved() {
        // given
        when(reservationRepository.existsByConferenceRoomId(anyLong()))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> conferenceRoomService.deleteById(1L))
                .isInstanceOf(BadRequestException.class);

        verify(conferenceRoomRepository, never()).deleteById(1L);
    }
}
