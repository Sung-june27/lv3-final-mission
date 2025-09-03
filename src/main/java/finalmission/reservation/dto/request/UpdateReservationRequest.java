package finalmission.reservation.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

public record UpdateReservationRequest(
        @NotNull LocalDate date,
        @NotNull LocalTime time,
        @NotNull Long conferenceRoomId
) {
}
