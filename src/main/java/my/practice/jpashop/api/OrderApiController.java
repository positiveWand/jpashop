package my.practice.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.Order;
import my.practice.jpashop.domain.OrderItem;
import my.practice.jpashop.domain.OrderStatus;
import my.practice.jpashop.repository.OrderRepository;
import my.practice.jpashop.repository.OrderSearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        // 엔티티 직접 노출

        // @JsonIgnore 필요: 양방향 연관관계로 인한 무한루프 방지 - 프로젝트에는 적용되어 있지 않음
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            // 연관 객체 초기화
            order.getMember().getName();
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return all;
    }
}
