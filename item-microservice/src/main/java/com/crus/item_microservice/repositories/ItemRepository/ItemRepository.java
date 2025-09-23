package com.crus.item_microservice.repositories.ItemRepository;

import com.crus.item_microservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findByName(String name);

}
