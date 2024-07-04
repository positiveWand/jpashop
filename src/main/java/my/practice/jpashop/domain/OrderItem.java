package my.practice.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.practice.jpashop.domain.item.Item;

@Entity
@Getter @Setter
public class OrderItem {
    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item, int orderPrice, int count) {
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count);
        return orderItem;
    }
    
    //==비즈니스 로직==//
    public void cancel() {
        this.getItem().addStock(this.count);
    }

    //==조회 로직==//
    public int getTotalPrice() {
        return this.getOrderPrice() * this.getCount();
    }
}
