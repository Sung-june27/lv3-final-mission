package finalmission.member.service;

import finalmission.global.config.JwtTokenProvider;
import finalmission.global.error.exception.BadRequestException;
import finalmission.member.domain.LoginMember;
import finalmission.member.domain.Member;
import finalmission.member.domain.Role;
import finalmission.member.dto.request.LoginRequest;
import finalmission.member.repository.MemberRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtTokenProvider jwtTokenProvider;
    private final MemberRepository memberRepository;

    public String login(LoginRequest request) {
        Member member = getMemberByEmail(request.email());

        validatePasswordCorrect(request, member);

        return jwtTokenProvider.createTokenByMember(member);
    }

    private void validatePasswordCorrect(LoginRequest request, Member member) {
        if (!member.matchesPassword(request.password())) {
            throw new BadRequestException("이메일 또는 비밀번호가 일치하지 않습니다.");
        }
    }

    private Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new BadRequestException("이메일 또는 비밀번호가 일치하지 않습니다."));
    }

    public LoginMember getLoginMemberByToken(String token) {
        Claims claims = jwtTokenProvider.getClaimsFromToken(token);

        Long userId = Long.valueOf(claims.getSubject());
        String name = claims.get("name", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));

        return new LoginMember(userId, name, role);
    }
}
