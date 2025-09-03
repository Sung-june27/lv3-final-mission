package finalmission.room.controller;

import finalmission.global.config.CheckRole;
import finalmission.member.domain.Role;
import finalmission.room.dto.request.CreateRoomRequest;
import finalmission.room.dto.response.CreateRoomResponse;
import finalmission.room.service.ConferenceRoomService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room")
public class ConferenceRoomController {

    private final ConferenceRoomService conferenceRoomService;

    @GetMapping
    public ResponseEntity<List<ReadRoomResponse>> getAllConferenceRoom() {
        List<ReadRoomResponse> responses = conferenceRoomService.findAll();

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    @CheckRole(Role.ADMIN)
    public ResponseEntity<CreateRoomResponse> createConferenceRoom(
            @RequestBody CreateRoomRequest request
    ) {
        CreateRoomResponse response = conferenceRoomService.create(request);

        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    @CheckRole(Role.ADMIN)
    public ResponseEntity<Void> deleteConferenceRoomById(@PathVariable Long id) {
        conferenceRoomService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
