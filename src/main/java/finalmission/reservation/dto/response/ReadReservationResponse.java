package finalmission.reservation.dto.response;

import finalmission.reservation.domain.Reservation;
import java.time.LocalDate;
import java.time.LocalTime;

public record ReadReservationResponse(
        Long id,
        LocalDate date,
        LocalTime time,
        String conferenceRoomName,
        String memberName
) {

    public static ReadReservationResponse from(Reservation reservation) {
        return new ReadReservationResponse(
                reservation.getId(),
                reservation.getDate(),
                reservation.getTime(),
                reservation.getConferenceRoom().getName(),
                reservation.getMember().getName()
        );
    }
}
