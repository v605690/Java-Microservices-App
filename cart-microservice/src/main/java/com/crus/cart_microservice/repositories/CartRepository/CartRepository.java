package com.crus.cart_microservice.repositories.CartRepository;

import com.crus.cart_microservice.entities.Cart;
import com.crus.cart_microservice.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);

}
