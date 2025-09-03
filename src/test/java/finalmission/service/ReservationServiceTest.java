package finalmission.service;

import static finalmission.helper.TestFixture.CONFERENCE_ROOM;
import static finalmission.helper.TestFixture.DEFAULT_TIME;
import static finalmission.helper.TestFixture.LOGIN_MEMBER;
import static finalmission.helper.TestFixture.MEMBER;
import static finalmission.helper.TestFixture.OTHER_MEMBER;
import static finalmission.helper.TestFixture.RESERVATION;
import static finalmission.helper.TestFixture.TOMORROW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import finalmission.external.HolidayService;
import finalmission.global.error.exception.BadRequestException;
import finalmission.member.service.MemberService;
import finalmission.reservation.domain.Reservation;
import finalmission.reservation.dto.request.CreateReservationRequest;
import finalmission.reservation.dto.request.UpdateReservationRequest;
import finalmission.reservation.dto.response.CreateReservationResponse;
import finalmission.reservation.dto.response.ReservationByMemberResponse;
import finalmission.reservation.dto.response.UpdateReservationResponse;
import finalmission.reservation.repository.ReservationRepository;
import finalmission.reservation.service.ReservationService;
import finalmission.room.service.ConferenceRoomService;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private HolidayService holidayService;

    @Mock
    private ConferenceRoomService conferenceRoomService;

    @Mock
    private MemberService memberService;

    @Mock
    private ReservationRepository reservationRepository;

    @DisplayName("예약을 추가한다.")
    @Test
    void create() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                TOMORROW,
                DEFAULT_TIME,
                1L
        );
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);
        when(conferenceRoomService.getById(anyLong()))
                .thenReturn(CONFERENCE_ROOM);
        when(reservationRepository.save(any(Reservation.class)))
                .thenReturn(RESERVATION);

        // when
        CreateReservationResponse response = reservationService.create(request, LOGIN_MEMBER);

        // then
        assertAll(
                () -> assertThat(response.date()).isEqualTo(TOMORROW),
                () -> assertThat(response.time()).isEqualTo(DEFAULT_TIME),
                () -> assertThat(response.conferenceRoomName()).isEqualTo(CONFERENCE_ROOM.getName()),
                () -> assertThat(response.memberName()).isEqualTo(MEMBER.getName())
        );

        verify(reservationRepository).save(any(Reservation.class));
    }

    @DisplayName("예약이 이미 존재한다면, 예외를 발생한다")
    @Test
    void create_WhenExistsReservation() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                TOMORROW,
                DEFAULT_TIME,
                1L
        );
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);
        when(conferenceRoomService.getById(anyLong()))
                .thenReturn(CONFERENCE_ROOM);
        when(reservationRepository.existsByDateAndTimeAndConferenceRoom(any(), any(), any()))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request, LOGIN_MEMBER))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("공휴일에는 예약을 할 수 없다.")
    @Test
    void create_WhenHoliday() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                LocalDate.of(2020, 6, 6),
                DEFAULT_TIME,
                1L
        );
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);
        when(conferenceRoomService.getById(anyLong()))
                .thenReturn(CONFERENCE_ROOM);
        when(holidayService.isHoliday(any()))
                .thenReturn(true);

        assertThatThrownBy(() -> reservationService.create(request, LOGIN_MEMBER))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("과거로 예약을 시도한다면, 예외를 던진다.")
    @Test
    void create_WhenPastDateTime() {
        // given
        CreateReservationRequest request = new CreateReservationRequest(
                LocalDate.now().minusDays(1),
                DEFAULT_TIME,
                1L
        );
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);
        when(conferenceRoomService.getById(anyLong()))
                .thenReturn(CONFERENCE_ROOM);

        // when & then
        assertThatThrownBy(() -> reservationService.create(request, LOGIN_MEMBER))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("모든 예약 정보를 가져온다.")
    @Test
    void findAll() {
        // given
        List<Reservation> reservations = createReservations();
        when(reservationRepository.findAll())
                .thenReturn(reservations);

        // when & then
        assertThatCode(() -> reservationService.findALl())
                .doesNotThrowAnyException();
    }

    @DisplayName("사용자는 본인의 예약 목록만 조회할 수 있다.")
    @Test
    void findAllByMember() {
        // given
        List<Reservation> reservations = createReservations();
        when(reservationRepository.findAllByMemberId(anyLong()))
                .thenReturn(reservations);

        // when
        List<ReservationByMemberResponse> responses = reservationService.findAllByMember(LOGIN_MEMBER);

        // then
        boolean allMatchByMember = responses.stream()
                .allMatch(response -> Objects.equals(response.memberName(), MEMBER.getName()));

        assertThat(allMatchByMember).isEqualTo(true);
    }

    @DisplayName("본인의 예약을 수정한다.")
    @Test
    void updateByMember() {
        // given
        UpdateReservationRequest request = new UpdateReservationRequest(
                TOMORROW.plusDays(1),
                LocalTime.of(11, 0),
                1L
        );
        Reservation reservation = new Reservation(1L, TOMORROW, DEFAULT_TIME, CONFERENCE_ROOM, MEMBER);

        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.of(reservation));
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);
        when(conferenceRoomService.getById(anyLong()))
                .thenReturn(CONFERENCE_ROOM);

        // when
        UpdateReservationResponse response = reservationService.updateByMember(1L, request, LOGIN_MEMBER);

        // then
        assertAll(
                () -> assertThat(response.date()).isEqualTo(TOMORROW.plusDays(1)),
                () -> assertThat(response.time()).isEqualTo(LocalTime.of(11, 0))
        );
    }

    @DisplayName("본인의 예약이 아니라면, 수정할 수 없다.")
    @Test
    void updateByMember_WhenNotMine() {
        // given
        UpdateReservationRequest request = new UpdateReservationRequest(
                TOMORROW.plusDays(1),
                LocalTime.of(11, 0),
                1L
        );

        Reservation reservation = new Reservation(1L, TOMORROW, DEFAULT_TIME, CONFERENCE_ROOM, OTHER_MEMBER);

        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.of(reservation));
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);
        when(conferenceRoomService.getById(anyLong()))
                .thenReturn(CONFERENCE_ROOM);

        // when & then
        assertThatThrownBy(() -> reservationService.updateByMember(1L, request, LOGIN_MEMBER))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("공휴일에는 수정할 수 없다.")
    @Test
    void updateByMember_WhenHoliday() {
        // given
        UpdateReservationRequest request = new UpdateReservationRequest(
                LocalDate.of(2020, 6, 6),
                LocalTime.of(11, 0),
                1L
        );
        Reservation reservation = new Reservation(1L, TOMORROW, DEFAULT_TIME, CONFERENCE_ROOM, MEMBER);

        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.of(reservation));
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);
        when(conferenceRoomService.getById(anyLong()))
                .thenReturn(CONFERENCE_ROOM);
        when(holidayService.isHoliday(any()))
                .thenReturn(true);

        // when & then
        assertThatThrownBy(() -> reservationService.updateByMember(1L, request, LOGIN_MEMBER))
                .isInstanceOf(BadRequestException.class);
    }

    @DisplayName("본인의 예약이라면, 삭제할 수 있다.")
    @Test
    void deleteByMember() {
        // given
        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.of(RESERVATION));
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);

        // when & then
        assertThatCode(() -> reservationService.deleteByMember(1L, LOGIN_MEMBER))
                .doesNotThrowAnyException();

        verify(reservationRepository).delete(RESERVATION);
    }

    @DisplayName("본인의 예약이 아니라면, 삭제할 수 없다.")
    @Test
    void deleteByMember_WhenNotMine() {
        // given
        Reservation reservation = new Reservation(1L, TOMORROW, DEFAULT_TIME, CONFERENCE_ROOM, OTHER_MEMBER);

        when(reservationRepository.findById(anyLong()))
                .thenReturn(Optional.of(reservation));
        when(memberService.getById(anyLong()))
                .thenReturn(MEMBER);

        // when & then
        assertThatThrownBy(() -> reservationService.deleteByMember(1L, LOGIN_MEMBER))
                .isInstanceOf(BadRequestException.class);
    }

    private List<Reservation> createReservations() {
        List<Reservation> reservations = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            LocalDate date = LocalDate.now().plusDays(i);
            Reservation reservation = new Reservation((long) i, date, DEFAULT_TIME, CONFERENCE_ROOM, MEMBER);
            reservations.add(reservation);
        }
        return reservations;
    }
}
