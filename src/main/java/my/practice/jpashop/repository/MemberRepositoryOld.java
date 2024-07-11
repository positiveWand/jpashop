package my.practice.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import my.practice.jpashop.domain.Member;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class MemberRepositoryOld {
    @PersistenceContext
    private EntityManager em; // Spring이 EntityManager 생성 후 주입

    // 엔티티 저장
    public void save(Member member) {
        em.persist(member);
    }

    // 엔티티 단건 검색 with ID
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }
    
    // 엔티티 전체 검색
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 엔티티 검색 with 이름
    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
