package my.practice.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.Order;
import my.practice.jpashop.domain.OrderStatus;
import my.practice.jpashop.repository.OrderRepository;
import my.practice.jpashop.repository.OrderSearch;
import my.practice.jpashop.repository.order.simplequery.SimpleOrderQueryDto;
import my.practice.jpashop.repository.order.simplequery.SimpleOrderQueryRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;
    private final SimpleOrderQueryRepository simpleOrderQueryRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        // 문제점1 - 양방향 연관관계로 인해, JSON 생성시 무한루프 발생
        // 해결 - jackson 라이브러리의 @JsonIgnore 사용
        
        // 문제점2 - 지연 로딩으로 인한 엔티티 조회 문제: 실제 엔티티 객체 대신 프록시 객체를 반환 -> 오류 발생
        // 해결 - 프록시 객체 순회를 무시하는 라이브러리(Hibernate5Module) 사용, 지연 로딩이 수행되도록 코드 작성
        // 해결2 - 별도 DTO 사용
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        // 문제점 - 쿼리문이 다량 발생(N+1 문제): Order(1) + Member(N) + Delivery(N)
        // EAGER fetch를 해도 별효과 없음 -> 쿼리 여러번 발생
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<SimpleOrderDto> result =  orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        // 1번의 fetch join 쿼리로 Order, Member, Delivery 모두 조회
        // 연관객체를 이미 쿼리했기 때문에 지연로딩이 발생하지 않음 -> N+1 문제 해결
        // SQL, 맵핑을 이용해 하는 방법보다 훨씬 간편
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v4/simple-orders")
    public List<SimpleOrderQueryDto> orderV4() {
        // fetch join과 쿼리문이 유사하지만 정말 필요한만큼만 select하여 받아온다
        // v3 vs v4: v4가 더 최적화되었지만 재사용성 감소 - 한정적인 사용
        return simpleOrderQueryRepository.findOrderDtos();
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); // LAZY fetch
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); // LAZY fetch
        }
    }
}
