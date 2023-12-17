package com.qpassessment.service;

import com.qpassessment.entities.Item;
import com.qpassessment.repositories.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public Item createItem(Item item) {
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public Item updateItem(Item item) {
        return itemRepository.saveAndFlush(item);
    }

    @Override
    public void deleteItem(Long id) throws Exception{
        itemRepository.deleteById(id);
    }

    @Override
    public Optional<Item> getItem(Long id) {
        return itemRepository.findById(id);
    }

    @Override
    public List<Item> getItems() {
        return itemRepository.findAll();
    }
}
