package my.practice.jpashop.repository.order.simplequery;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SimpleOrderQueryRepository {
    private final EntityManager em;

    public List<SimpleOrderQueryDto> findOrderDtos() {
        // new 명령어를 활용해 JPQL 결과를 DTO로 맵핑

        return em.createQuery(
                        "select new my.practice.jpashop.repository.simplequery.SimpleOrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address) o from Order o" +
                                " join o.member m" +
                                " join o.delivery d", SimpleOrderQueryDto.class)
                .getResultList();
    }
}
