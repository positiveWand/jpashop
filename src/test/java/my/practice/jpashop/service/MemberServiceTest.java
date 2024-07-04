package my.practice.jpashop.service;

import my.practice.jpashop.domain.Member;
import my.practice.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional // SpringBoot - 테스트 케이스에서 트랜잭션 사용 시 테스트 후 롤백 진행
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(false) // 트랜잭션 롤백 비활성화
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setName("kim");

        Long savedId = memberService.join(member);

        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test
    public void 중복_회원이_존재하면_예외발생() throws Exception {
        Member member1 = new Member();
        member1.setName("kim");
        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member1);
//        try {
//            memberService.join(member2); // 예외 발생 기대
//        } catch(IllegalStateException e) {
//            return;
//        }
//        fail("예외가 발생하지 않음");
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2); // 예외 발생 기대
        });
    }

}