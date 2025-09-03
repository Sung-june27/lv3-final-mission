package finalmission.reservation.domain;

import finalmission.member.domain.Member;
import finalmission.room.domain.ConferenceRoom;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate date;

    private LocalTime time;

    @ManyToOne(fetch = FetchType.LAZY)
    private ConferenceRoom conferenceRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    public Reservation(LocalDate date, LocalTime time, ConferenceRoom conferenceRoom, Member member) {
        this(null, date, time, conferenceRoom, member);
    }

    public void update(LocalDate date, LocalTime time, ConferenceRoom conferenceRoom) {
        this.date = date;
        this.time = time;
        this.conferenceRoom = conferenceRoom;
    }

    public boolean isMine(Member member) {
        return this.member.equals(member);
    }
}
