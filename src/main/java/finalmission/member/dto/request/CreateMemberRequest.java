package finalmission.member.dto.request;

import finalmission.member.domain.Member;
import finalmission.member.domain.Role;

public record CreateMemberRequest(
        String name,
        String email,
        String password
) {
    public Member toMember() {
        return new Member(
                name,
                email,
                password,
                Role.USER
        );
    }
}
