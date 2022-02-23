package com.example.itemservice.domain.item;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ItemRepository2Test {

    ItemRepository itemRepository = new ItemRepository();

    @AfterEach
    void afterEach() {
        itemRepository.clearStore();
    }

    @Test
    void testSave() {
        // Given
        Item item = new Item("itemA", 10000, 10);

        // When
        var itemPersisted = itemRepository.save(item);

        // Then
        var itemRetrieved = itemRepository.findById(itemPersisted.getId());
        System.out.println(itemPersisted);
        System.out.println(itemRetrieved);
        assertThat(itemRetrieved).isEqualTo(itemPersisted);
    }

    @Test
    void testFindAll() {
        // Given
        Item item1 = new Item("item1", 10000, 10);
        Item item2 = new Item("item2", 10000, 10);

        itemRepository.save(item1);
        itemRepository.save(item2);

        // When
        var itemsRetrieved = itemRepository.findAll();

        // Then
        assertThat(itemsRetrieved.size()).isEqualTo(2);
        assertThat(itemsRetrieved).contains(item1, item2);
    }

    @Test
    void testUpdate() {
        // Given
        Item item1 = new Item("item1", 10000, 10);
        var itemPersisted = itemRepository.save(item1);
        var id = itemPersisted.getId();

        // When
        var item2 = new Item("item2", 10001, 11);
        itemRepository.update(id, item2);
        var itemUpdated = itemRepository.findById(id);

        // Then
        assertThat(itemUpdated.getItemName()).isEqualTo(item2.getItemName());
        assertThat(itemUpdated.getPrice()).isEqualTo(item2.getPrice());
        assertThat(itemUpdated.getQuantity()).isEqualTo(item2.getQuantity());
    }

}