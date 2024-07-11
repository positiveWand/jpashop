package my.practice.jpashop.repository;

import my.practice.jpashop.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByName(String name); // Spring Data JPA가 메서드 이름을 분석하여 알아서 JPQL 쿼리 생성
}
