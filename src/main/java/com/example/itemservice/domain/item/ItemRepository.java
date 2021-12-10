package com.example.itemservice.domain.item;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Repository
public class ItemRepository {

    private static final ConcurrentMap<Long, Item> store = new ConcurrentHashMap<>();
    private static long sequence = 0L;

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);

        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values()); // Deep copy
    }

    public void update(Long itemId, Item updateParameter) { // Don't use entity like next to
        var item = findById(itemId);
        item.setItemName(updateParameter.getItemName());
        item.setPrice(updateParameter.getPrice());
        item.setQuantity(updateParameter.getQuantity());
        item.setOpen(updateParameter.getOpen());
        item.setRegions(updateParameter.getRegions());
        item.setItemType(updateParameter.getItemType());
        item.setDeliveryCode(updateParameter.getDeliveryCode());
    }

    public void clearStore() {
        store.clear();
    }
}
