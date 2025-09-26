package com.crus.client_application.service;

import com.crus.client_application.model.Cart;
import com.crus.client_application.model.CartItem;
import com.crus.client_application.model.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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

}
