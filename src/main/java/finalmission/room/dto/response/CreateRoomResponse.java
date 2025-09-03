package finalmission.room.dto.response;

import finalmission.room.domain.ConferenceRoom;

public record CreateRoomResponse(
        Long id,
        String name
) {
    public static CreateRoomResponse from(ConferenceRoom conferenceRoom) {
        return new CreateRoomResponse(
                conferenceRoom.getId(),
                conferenceRoom.getName()
        );
    }
}
