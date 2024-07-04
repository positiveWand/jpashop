package my.practice.jpashop.service;

import jakarta.persistence.EntityManager;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.Member;
import my.practice.jpashop.domain.Order;
import my.practice.jpashop.domain.OrderStatus;
import my.practice.jpashop.domain.exception.NotEnoughStockException;
import my.practice.jpashop.domain.item.Book;
import my.practice.jpashop.domain.item.Item;
import my.practice.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;



    @Test
    public void 상품주문() throws Exception {
        Member member = createMember();

        Book book = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        Order expectedOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.ORDER, expectedOrder.getStatus(), "상품 주문시 상태는 ORDER");
        assertEquals(1, expectedOrder.getOrderItems().size(), "주문한 상품 종류 수가 정확해야한다");
        assertEquals(10000 * orderCount, expectedOrder.getTotalPrice(), "주문 가격은 가격 * 수량이다");
        assertEquals(8, book.getStockQuantity(), "주문 수량만큼 재고가 줄어야한다");
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "rkdrk", "123-123"));
        em.persist(member);
        return member;
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        Member member = createMember();

        Item item = createBook("시골 JPA", 10000, 10);

        int orderCount = 11;

        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), item.getId(), orderCount);
        });
    }

    @Test
    public void 주문취소() throws Exception {
        Member member = createMember();
        Book item = createBook("시골 JPA", 10000, 10);

        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        orderService.cancelOrder(orderId);
        Order expectedOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, expectedOrder.getStatus(), "주문 취소시 상태는 CANCEL 이다");
        assertEquals(10, item.getStockQuantity(), "주문이 취소된 상품은 소모된 재고가 복구되어야한다");
    }

}