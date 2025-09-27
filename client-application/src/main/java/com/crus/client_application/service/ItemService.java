package com.crus.client_application.service;

import com.crus.client_application.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static com.crus.client_application.model.Item.deletedItem;

@Service
public class ItemService {

    @Autowired
    @LoadBalanced
    private RestTemplate restTemplate;

    public List<Item> getAllItems() {
        String[] possiblePaths = {
                "http://item-microservice/item"
        };

        for (String url : possiblePaths) {
        try {
            System.out.println("Trying URL: " + url);

            ResponseEntity<Item[]> response = restTemplate.getForEntity(
                    url, Item[].class);
            if (response.getStatusCode().is2xxSuccessful()
                    && response.getBody() != null) {
                return Arrays.asList(response.getBody());
            } else {
                return List.of();
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch items: " + e.getMessage());
        }
    }
            return List.of();
    }

    public Item getItemById(Long itemId) {
        try {
            ResponseEntity<Item> response = restTemplate.getForEntity
                    ("http://item-microservice/item/" + itemId, Item.class);
            Item item = response.getBody();
            System.out.println("Item microservice returned: " + (item != null ? item.getName() : "null"));
            return item;
        } catch (Exception e) {
            System.out.println("Error fetching item " + itemId + ": " + e.getMessage());
            return null;
        }
    }
    public void deleteItem(Long itemId) {
        String url = "http://item-microservice/item/" + itemId;
        try {
            ResponseEntity<Void> response = restTemplate.exchange
                    (url, HttpMethod.DELETE, null, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Item successfully deleted: " + itemId);
            } else {
                System.out.println("Failed to delete item. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            throw e;
        }
    }
}
