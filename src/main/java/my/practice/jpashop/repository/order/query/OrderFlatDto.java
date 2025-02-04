package my.practice.jpashop.repository.order.query;

import lombok.Data;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderFlatDto {
    // Order 관련 칼럼
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus status;
    private Address address;

    // OrderItem 관련 칼럼
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus status, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.status = status;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
