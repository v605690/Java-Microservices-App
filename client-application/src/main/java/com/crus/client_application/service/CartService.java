package com.crus.client_application.service;

import com.crus.client_application.model.Cart;
import com.crus.client_application.model.CartItem;
import com.crus.client_application.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ItemService itemService;

    private Long convertUsernameToId(String username) {
        // Convert username to a consistent positive Long ID
        return Math.abs((long) username.hashCode());
    }

    public Cart addCartItem(Long userId, Long itemId) {

        String url = "http://cart-microservice/cart/" + userId + "?item-id=" + itemId;
        ResponseEntity<Cart> response = restTemplate.postForEntity(url, null, Cart.class);
        Cart cart = response.getBody();

        if (cart != null && cart.getItems() != null) {
            populateItemDetails(cart);
        }
        return cart;
    }

    public void removeCartItem(String username, Long itemId) {
        Long numUserId = convertUsernameToId(username);
        String url = "http://cart-microservice/cart/" + numUserId + "?item-id=" + itemId;

        System.out.println("Attempting to remove item " + itemId + " from cart for user " + numUserId + " (numUserId: " + numUserId + ")");
        System.out.println("URL: " + url);

        try {
        ResponseEntity<Void> response = restTemplate.exchange
                (url, HttpMethod.DELETE, null, Void.class);
            if (response.getStatusCode().is2xxSuccessful()) {
                System.out.println("Item successfully deleted: ");
            } else {
                System.out.println("Failed to delete item. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }

    }

    public Cart getCartByUserId(String username) {
        try {
            Long numUserId = convertUsernameToId(username);
            ResponseEntity<Cart> response = restTemplate.getForEntity
                    ("http://cart-microservice/cart/" + numUserId, Cart.class);
            Cart cart = response.getBody();

            if (cart != null && cart.getItems() != null) {
                populateItemDetails(cart);
            }

            return cart;

        } catch (Exception e) {
            return new Cart();
        }
    }
    private void populateItemDetails(Cart cart) {
        System.out.println("Populating item details for " + cart.getItems().size() + " items");

        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getItemId() != null) {
                try {
                    System.out.println("Looking up item with ID: " + cartItem.getItemId());
                    Item item = itemService.getItemById(cartItem.getItemId());
                    System.out.println("Found item: " + (item != null ? item.getName() : "null"));
                    cartItem.setItem(item);
                } catch (Exception e) {
                    System.out.println("Error looking up item " + cartItem.getItemId() + ": " + e.getMessage());
                }
            }
        }
    }
    public void increaseCartItem(String username, Long itemId) {
        Long userId = convertUsernameToId(username);
        addCartItem(userId, itemId);
    }

    public void decreaseCartItem(String username, Long itemId) {
        // Get the current cart to check the item's amount
        Cart cart = getCartByUserId(username);

        if (cart != null && cart.getItems() != null) {
            CartItem targetItem = cart.getItems().stream()
                    .filter(item -> item.getItemId().equals(itemId))
                    .findFirst()
                    .orElse(null);

            if (targetItem != null) {
                if (targetItem.getAmount() > 1) {
                    // Item has more than 1, decrease by 1
                    // Remove completely, then add back amount -1 times
                    removeCartItem(username, itemId);

                    Long userId = convertUsernameToId(username);
                    int newAmount = targetItem.getAmount() -1;
                    for (int i = 0; i < newAmount; i++) {
                        addCartItem(userId, itemId);
                    }
                } else {
                    // Item has amount = 1, remove
                    removeCartItem(username, itemId);
                }
            }
        }
    }
}
