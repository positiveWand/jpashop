package my.practice.jpashop.repository;

import lombok.Getter;
import lombok.Setter;
import my.practice.jpashop.domain.OrderStatus;

@Getter @Setter
public class OrderSearch {
    private String memberName;
    private OrderStatus orderStatus;
}
