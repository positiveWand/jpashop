package my.practice.jpashop.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.item.Item;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor // Lombok 생성자 - 필수적인 값(final)을 채우는 생성자
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if(item.getId() == null) { // 영속되지 않은 경우
            em.persist(item);
        } else { // 영속된 경우
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
