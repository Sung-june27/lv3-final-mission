package finalmission.reservation.service;

import finalmission.external.HolidayService;
import finalmission.global.error.exception.BadRequestException;
import finalmission.global.error.exception.NotFoundException;
import finalmission.member.domain.LoginMember;
import finalmission.member.domain.Member;
import finalmission.member.service.MemberService;
import finalmission.reservation.domain.Reservation;
import finalmission.reservation.dto.request.CreateReservationRequest;
import finalmission.reservation.dto.request.UpdateReservationRequest;
import finalmission.reservation.dto.response.CreateReservationResponse;
import finalmission.reservation.dto.response.ReadReservationResponse;
import finalmission.reservation.dto.response.ReservationByMemberResponse;
import finalmission.reservation.dto.response.UpdateReservationResponse;
import finalmission.reservation.repository.ReservationRepository;
import finalmission.room.domain.ConferenceRoom;
import finalmission.room.service.ConferenceRoomService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ReservationService {

    private final HolidayService holidayService;
    private final MemberService memberService;
    private final ConferenceRoomService conferenceRoomService;

    private final ReservationRepository reservationRepository;

    public CreateReservationResponse create(CreateReservationRequest request, LoginMember loginMember) {
        ConferenceRoom conferenceRoom = conferenceRoomService.getById(request.conferenceRoomId());
        Member member = memberService.getById(loginMember.id());

        validateHoliday(request.date());
        validatePastDateTime(request.date(), request.time());
        validateAlreadyReserved(request, conferenceRoom);

        Reservation reservation = request.toReservation(conferenceRoom, member);
        Reservation saved = reservationRepository.save(reservation);

        return CreateReservationResponse.from(saved);
    }

    @Transactional(readOnly = true)
    public List<ReadReservationResponse> findALl() {
        List<Reservation> reservations = reservationRepository.findAll();
        return reservations.stream()
                .map(ReadReservationResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ReservationByMemberResponse> findAllByMember(LoginMember loginMember) {
        List<Reservation> reservations = reservationRepository.findAllByMemberId(loginMember.id());
        return reservations.stream()
                .map(ReservationByMemberResponse::from)
                .toList();
    }

    public UpdateReservationResponse updateByMember(Long reservationId, UpdateReservationRequest request, LoginMember loginMember) {
        Reservation reservation = getReservationById(reservationId);
        ConferenceRoom conferenceRoom = conferenceRoomService.getById(request.conferenceRoomId());
        Member member = memberService.getById(loginMember.id());

        validateHoliday(request.date());
        validateByMember(reservation, member);

        reservation.update(request.date(), request.time(), conferenceRoom);
        return UpdateReservationResponse.from(reservation);
    }

    public void deleteByMember(Long reservationId, LoginMember loginMember) {
        Reservation reservation = getReservationById(reservationId);
        Member member = memberService.getById(loginMember.id());
        validateByMember(reservation, member);
        reservationRepository.delete(reservation);
    }

    private void validateByMember(Reservation reservation, Member member) {
        if (!reservation.isMine(member)) {
            throw new BadRequestException("본인 예약만 수정 / 삭제 가능합니다.");
        }
    }

    private void validateHoliday(LocalDate date) {
        if (holidayService.isHoliday(date)) {
            throw new BadRequestException("공휴일에는 예약이 불가능합니다.");
        }
    }

    private void validatePastDateTime(LocalDate date, LocalTime time) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime dateTime = LocalDateTime.of(date, time);
        if (dateTime.isBefore(now)) {
            throw new BadRequestException("과거 날짜/시간으로는 불가능합니다.");
        }
    }

    private void validateAlreadyReserved(CreateReservationRequest request, ConferenceRoom conferenceRoom) {
        boolean alreadyReserved = reservationRepository.existsByDateAndTimeAndConferenceRoom(
                request.date(), request.time(), conferenceRoom);
        if (alreadyReserved) {
            throw new BadRequestException("예약이 이미 존재합니다.");
        }
    }

    private Reservation getReservationById(Long reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new NotFoundException("예약을 찾을 수 없습니다."));
    }
}
