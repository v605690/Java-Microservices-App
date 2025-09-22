package com.crus.cart_microservice.repositories.CartRepository;

import com.crus.cart_microservice.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long userId);
}
