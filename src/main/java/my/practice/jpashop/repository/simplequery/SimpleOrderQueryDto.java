package my.practice.jpashop.repository.simplequery;

import lombok.Data;
import my.practice.jpashop.domain.Address;
import my.practice.jpashop.domain.OrderStatus;

import java.time.LocalDateTime;

@Data
public class SimpleOrderQueryDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    public SimpleOrderQueryDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address) {
        this.orderId = orderId;
        this.name = name; // LAZY fetch
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address; // LAZY fetch
    }
}
