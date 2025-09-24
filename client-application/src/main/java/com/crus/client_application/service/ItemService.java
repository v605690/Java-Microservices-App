package com.crus.client_application.service;

import com.crus.client_application.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
public class ItemService {

    @Autowired
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
}
