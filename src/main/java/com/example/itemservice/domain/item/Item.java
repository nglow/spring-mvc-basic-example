package com.example.itemservice.domain.item;

import lombok.*;
import org.hibernate.validator.constraints.Range;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

//@ScriptAssert(lang = "javascript", script = "_this.price * _this.quantity >= 10000", message = "10000원 넘게 입력해 주세요")
@Getter
@Setter
public class Item {

//    @NotNull(groups = UpdateCheck.class)
    private Long id;

//    @NotBlank(groups = {SaveCheck.class, UpdateCheck.class})
    private String itemName;

//    @NotNull(groups={SaveCheck.class, UpdateCheck.class})
//    @Range(min = 1000, max = 1000000, groups = {SaveCheck.class, UpdateCheck.class})
    private Integer price;

//    @NotNull(groups = {SaveCheck.class, UpdateCheck.class})
//    @Max(value = 9999, groups = {SaveCheck.class})
    private Integer quantity;

    private Boolean open;
    private List<String> regions;
    private ItemType itemType;
    private String deliveryCode;

    public Item() {
    }

    public Item(String itemName, Integer price, Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", itemName='" + itemName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", open=" + open +
                ", regions=" + regions +
                ", itemType=" + itemType +
                ", deliveryCode='" + deliveryCode + '\'' +
                '}';
    }
}
