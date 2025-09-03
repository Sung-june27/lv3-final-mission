package finalmission.member.controller;

import finalmission.member.dto.request.CreateMemberRequest;
import finalmission.member.dto.response.CreateMemberResponse;
import finalmission.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<CreateMemberResponse> createMember(@RequestBody CreateMemberRequest request) {
        CreateMemberResponse response = memberService.create(request);

        return ResponseEntity.ok().body(response);
    }
}
