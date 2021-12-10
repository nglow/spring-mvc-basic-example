package com.example.itemservice.domain.item;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class Item {

    private Long id;
    private String itemName;
    private Integer price;
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
}
