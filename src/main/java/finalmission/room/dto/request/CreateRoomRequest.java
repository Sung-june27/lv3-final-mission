package finalmission.room.dto.request;

import finalmission.room.domain.ConferenceRoom;

public record CreateRoomRequest(
        String name
) {

    public ConferenceRoom toConferenceRoom() {
        return new ConferenceRoom(name);
    }
}
