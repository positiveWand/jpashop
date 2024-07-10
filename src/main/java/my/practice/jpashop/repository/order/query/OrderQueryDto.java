package my.practice.jpashop.repository.order.query;

import lombok.Data;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Address address;
    private List<OrderItemQueryDto> orderItems;

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus status, Address address) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.status = status;
        this.address = address;
//        this.orderItems = orderItems; // 컬렉션은 DTO로 곧바로 맵핑이 불가하므로
    }
}
