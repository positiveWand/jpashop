package my.practice.jpashop.domain.item;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import my.practice.jpashop.domain.Category;
import my.practice.jpashop.domain.exception.NotEnoughStockException;

import java.util.ArrayList;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
@Getter @Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    // 재고 늘리기
    public void addStock(int quantity) {
        this.stockQuantity += quantity;
    }

    // 재고 줄이기
    public void removeStock(int quantity) {
        int remainStock = this.stockQuantity - quantity;
        if(remainStock < 0) {
            throw new NotEnoughStockException("stock cannot be below zero");
        }

        this.stockQuantity = remainStock;
    }
}
