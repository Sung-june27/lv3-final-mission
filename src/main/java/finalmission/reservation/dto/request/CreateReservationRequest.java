package finalmission.reservation.dto.request;

import finalmission.member.domain.Member;
import finalmission.reservation.domain.Reservation;
import finalmission.room.domain.ConferenceRoom;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record CreateReservationRequest(
        @NotNull LocalDate date,
        @NotNull LocalTime time,
        @NotNull Long conferenceRoomId
) {
    public Reservation toReservation(ConferenceRoom conferenceRoom, Member member) {
        return new Reservation(date, time, conferenceRoom, member);
    }
}
