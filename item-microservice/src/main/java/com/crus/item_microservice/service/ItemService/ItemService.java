package com.crus.item_microservice.service.ItemService;

import com.crus.item_microservice.entity.Item;
import com.crus.item_microservice.repositories.ItemRepository.ItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {

    @Autowired
    ItemRepository itemRepository;

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItemById(Long id) {
        return itemRepository.findById(id)
                .orElse(null);
    }

    public Item insertNewItem(Item newItem) {
        newItem.setName(newItem.getName());
        return itemRepository.save(newItem);
    }

    public void deleteItemById(Long id) {
        itemRepository.deleteItemByItemId(id);
    }

    public Item updateItem(Item updatedItem) {
        Item item = itemRepository.findByName(updatedItem.getName());
        BeanUtils.copyProperties(updatedItem, item);
        return itemRepository.save(item);
    }
}
