package my.practice.jpashop.service;

import my.practice.jpashop.domain.Member;
import my.practice.jpashop.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired // 생성자 주입 - 생략 가능(Spring에서 자체판단하여 주입)
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
    
    // 회원 가입
    @Transactional // Transactional 설정 오버라이드
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> foundMembers =  memberRepository.findByName(member.getName());

        if(!foundMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
