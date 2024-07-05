package my.practice.jpashop.service;

import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.item.Book;
import my.practice.jpashop.domain.item.Item;
import my.practice.jpashop.repository.ItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;

    @Transactional // 쓰기 트랜잭션을 위한 설정 오버라이딩
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long itemId, int price, String name, int stockQuantity) {
        Item item = itemRepository.findOne(itemId);
        // 이렇게 setter로 값을 설정하기보다는 엔티티에 수정 관련 도메인 로직을 내장시키는 것이 유지보수측면에서 유리
        item.setPrice(price);
        item.setName(name);
        item.setStockQuantity(stockQuantity);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
