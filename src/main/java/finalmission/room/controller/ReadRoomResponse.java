package finalmission.room.controller;

import finalmission.room.domain.ConferenceRoom;

public record ReadRoomResponse(
        Long id,
        String name
) {

    public static ReadRoomResponse from(ConferenceRoom conferenceRoom) {
        return new ReadRoomResponse(
                conferenceRoom.getId(),
                conferenceRoom.getName()
        );
    }
}
