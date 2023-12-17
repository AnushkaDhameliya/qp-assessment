package com.qpassessment.service;

import com.qpassessment.entities.Item;

import java.util.List;
import java.util.Optional;

public interface ItemService {
    public Item createItem(Item item);

    public Item updateItem(Item item);

    public void deleteItem(Long id) throws Exception;

    public Optional<Item> getItem(Long id);

    public List<Item> getItems();
}
