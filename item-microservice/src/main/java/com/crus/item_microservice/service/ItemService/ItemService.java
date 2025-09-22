package com.crus.item_microservice.service.ItemService;

import com.crus.item_microservice.entity.Item;
import com.crus.item_microservice.repositories.ItemRepository.ItemRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public void deleteById(Long id) {
        itemRepository.deleteById(id);
    }

    public Item updateItem(Item updatedItem) {
        Optional<Item> optionalItem = itemRepository.findById(updatedItem.getItemId());
        if (optionalItem.isEmpty()) {
            return null;
        }
        Item existingItem = optionalItem.get();
        BeanUtils.copyProperties(updatedItem, existingItem);
        return itemRepository.save(existingItem);
    }
}
