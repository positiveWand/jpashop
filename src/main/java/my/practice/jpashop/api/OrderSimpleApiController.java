package my.practice.jpashop.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.Order;
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
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

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

}
