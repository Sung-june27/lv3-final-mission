package finalmission.reservation.repository;

import finalmission.reservation.domain.Reservation;
import finalmission.room.domain.ConferenceRoom;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByMemberId(Long id);

    boolean existsByDateAndTimeAndConferenceRoom(LocalDate date, LocalTime time, ConferenceRoom conferenceRoom);

    boolean existsByConferenceRoomId(Long conferenceRoomId);
}
