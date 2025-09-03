package finalmission.helper;

import static finalmission.member.domain.Role.USER;

import finalmission.member.domain.LoginMember;
import finalmission.member.domain.Member;
import finalmission.member.domain.Role;
import finalmission.reservation.domain.Reservation;
import finalmission.room.domain.ConferenceRoom;
import java.time.LocalDate;
import java.time.LocalTime;

public class TestFixture {
    public static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
    public static final LocalTime DEFAULT_TIME = LocalTime.of(10, 0);

    public static final LoginMember LOGIN_MEMBER = new LoginMember(1L, "사용자", USER);
    public static final Member MEMBER = new Member("사용자", "user@email.com", "password", USER);
    public static final Member ADMIN = new Member("관리자", "admin@email.com", "password", Role.ADMIN);
    public static final Member OTHER_MEMBER = new Member(2L, "다른 사용자", "other@email.com", "password", Role.USER);
    public static final ConferenceRoom CONFERENCE_ROOM = new ConferenceRoom("회의실 1");
    public static final Reservation RESERVATION = new Reservation(1L, TOMORROW, DEFAULT_TIME, CONFERENCE_ROOM, MEMBER);
}
