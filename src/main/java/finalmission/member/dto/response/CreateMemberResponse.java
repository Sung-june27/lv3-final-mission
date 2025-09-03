package finalmission.member.dto.response;

import finalmission.member.domain.Member;
import finalmission.member.domain.Role;

public record CreateMemberResponse(
        Long id,
        String name,
        String email,
        Role role
) {
    public static CreateMemberResponse from(Member member) {
        return new CreateMemberResponse(
                member.getId(),
                member.getName(),
                member.getEmail(),
                member.getRole()
        );
    }
}
