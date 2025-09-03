package finalmission.room.service;

import finalmission.global.error.exception.BadRequestException;
import finalmission.global.error.exception.NotFoundException;
import finalmission.reservation.repository.ReservationRepository;
import finalmission.room.controller.ReadRoomResponse;
import finalmission.room.domain.ConferenceRoom;
import finalmission.room.dto.request.CreateRoomRequest;
import finalmission.room.dto.response.CreateRoomResponse;
import finalmission.room.repository.ConferenceRoomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ConferenceRoomService {

    private final ConferenceRoomRepository conferenceRoomRepository;
    private final ReservationRepository reservationRepository;

    @Transactional(readOnly = true)
    public ConferenceRoom getById(Long conferenceRoomId) {
        return conferenceRoomRepository.findById(conferenceRoomId)
                .orElseThrow(() -> new NotFoundException("회의실을 찾을 수 없습니다."));
    }

    public CreateRoomResponse create(CreateRoomRequest request) {
        ConferenceRoom conferenceRoom = request.toConferenceRoom();
        ConferenceRoom saved = conferenceRoomRepository.save(conferenceRoom);

        return CreateRoomResponse.from(saved);
    }

    public void deleteById(Long id) {
        if (reservationRepository.existsByConferenceRoomId(id)) {
            throw new BadRequestException("예약이 있는 회의실은 삭제할 수 없습니다.");
        }
        conferenceRoomRepository.deleteById(id);
    }

    public List<ReadRoomResponse> findAll() {
        List<ConferenceRoom> conferenceRooms = conferenceRoomRepository.findAll();
        return conferenceRooms.stream()
                .map(ReadRoomResponse::from)
                .toList();
    }
}
