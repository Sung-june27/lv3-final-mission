package finalmission.member.service;

import finalmission.global.error.exception.NotFoundException;
import finalmission.member.domain.Member;
import finalmission.member.dto.request.CreateMemberRequest;
import finalmission.member.dto.response.CreateMemberResponse;
import finalmission.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getById(Long memberId) {
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("멤버를 찾을 수 없습니다."));
    }

    public CreateMemberResponse create(CreateMemberRequest request) {
        Member member = request.toMember();
        Member saved = memberRepository.save(member);

        return CreateMemberResponse.from(saved);
    }
}
