package my.practice.jpashop.service;

import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.Delivery;
import my.practice.jpashop.domain.Member;
import my.practice.jpashop.domain.Order;
import my.practice.jpashop.domain.OrderItem;
import my.practice.jpashop.domain.item.Item;
import my.practice.jpashop.repository.ItemRepository;
import my.practice.jpashop.repository.MemberRepository;
import my.practice.jpashop.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    // 주문하기
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        // 회원(memberId)가 아이템(itemId)를 count만큼 주문
        
        // 엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);
        
        // 배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        
        // 주문상품 정보 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        
        // 주문정보 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        
        // 주문정보 저장
        orderRepository.save(order); // Order 엔티티만 영속해도 OrderItem, Delivery 연쇄 영속 - 영속성 전이
        return order.getId();
    }

    // 주문 취소하기
    @Transactional
    public void cancelOrder(Long orderId) {
        // 주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();
    }

    // 주문 검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//        return orderRepository.findAll(orderSearch);
//    }
}
