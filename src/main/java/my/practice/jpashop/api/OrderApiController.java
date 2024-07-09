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
import org.springframework.web.bind.annotation.RequestParam;
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
        // 문제점 - 결과 튜플 뻥튀기로 인해서... 중복 데이터로 인한 대역폭 낭비, 페이징 불가
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_paging(
            @RequestParam(value = "offset", defaultValue = "1") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        // ~ToOne에 있는 객체는 fetch join으로 조회 -> 페이징 쉽게 적용 가능
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        // 컬렉션 객체는 batch_fetch_size를 통해 설정한 size만큼 IN 쿼리로 조회 -> 연관된 객체를 지연로딩을 모아서 IN 쿼리를 이용해 가져옴
        // 추가) Hibernate 6.2부터는 IN 절 대신 array_contains 함수를 사용한다
        // v3에 비해 쿼리가 많지만 정말 필요한 정보만 쿼리
        // 1+N 쿼리 -> 1+1 쿼리로 최적화
        
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
