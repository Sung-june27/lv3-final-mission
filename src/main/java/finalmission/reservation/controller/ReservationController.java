package finalmission.reservation.controller;

import finalmission.member.domain.LoginMember;
import finalmission.reservation.dto.request.CreateReservationRequest;
import finalmission.reservation.dto.request.UpdateReservationRequest;
import finalmission.reservation.dto.response.CreateReservationResponse;
import finalmission.reservation.dto.response.ReadReservationResponse;
import finalmission.reservation.dto.response.ReservationByMemberResponse;
import finalmission.reservation.dto.response.UpdateReservationResponse;
import finalmission.reservation.service.ReservationService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @GetMapping
    public ResponseEntity<List<ReadReservationResponse>> getAllReservations() {
        List<ReadReservationResponse> responses = reservationService.findALl();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/mine")
    public ResponseEntity<List<ReservationByMemberResponse>> getAllReservationsByMember(
            LoginMember loginMember
    ) {
        List<ReservationByMemberResponse> responses = reservationService.findAllByMember(loginMember);

        return ResponseEntity.ok(responses);
    }

    @PostMapping
    public ResponseEntity<CreateReservationResponse> createReservation(
            @RequestBody @Valid CreateReservationRequest request,
            LoginMember loginMember
    ) {
        CreateReservationResponse response = reservationService.create(request, loginMember);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<UpdateReservationResponse> updateReservation(
            @RequestBody @Valid UpdateReservationRequest request,
            @PathVariable Long id,
            LoginMember loginMember
    ) {
        UpdateReservationResponse response = reservationService.updateByMember(id, request, loginMember);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReservation(
            @PathVariable Long id,
            LoginMember loginMember
    ) {
        reservationService.deleteByMember(id, loginMember);

        return ResponseEntity.noContent().build();
    }
}
