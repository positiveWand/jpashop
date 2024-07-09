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

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        // 엔티티를 DTO로 변환
        // 쿼리가 너무 많이 발생
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderDto> collect = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());

        return collect;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        // fetch join을 활용해 로딩 -> 쿼리 개수 훨씬 감소
        // 문제점 - 페이징 불가능(튜플 뻥튀기로 인해)
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @Data
    static class OrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
//        private List<OrderItem> orderItems; // [BAD] DTO 선언시 엔티티 사용하면 의미 없어짐!
        private List<OrderItemDto> orderItems; // [GOOD] 내부에 엔티티 대신 DTO 사용

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
//            order.getOrderItems().stream().forEach(o -> o.getItem().getName());
//            orderItems = order.getOrderItems();
            orderItems = order.getOrderItems().stream()
                    .map(orderItem -> new OrderItemDto(orderItem))
                    .collect(Collectors.toList());
        }
    }

    @Data
    static class OrderItemDto {
        private String itemName;
        private int orderPrice;
        private int count;
        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
