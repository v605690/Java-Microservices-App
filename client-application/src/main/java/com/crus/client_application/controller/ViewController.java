package com.crus.client_application.controller;

import com.crus.client_application.model.Cart;
import com.crus.client_application.model.User;
import com.crus.client_application.service.CartService;
import com.crus.client_application.service.ItemService;
import com.crus.client_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class ViewController {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    CartService cartService;

    @GetMapping("/")
    public String ViewHomePage(Model model) {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registrationForm(@ModelAttribute("user") User user,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            System.out.println("Validation errors found");
            bindingResult.getAllErrors().forEach(error ->
                    System.out.println("Validation error: " + error.getDefaultMessage()));
            return "register";
        }
        try {
            userService.registerNewUser(user);
            System.out.println("User registered successfully");
            return "redirect:/login";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        } catch (Exception e) {
            model.addAttribute("errormessage", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }

    @GetMapping("/items")
    public String displayItems(Model model) {
        model.addAttribute("items", itemService.getAllItems());
        return "item-list";
    }

    @PostMapping("/cart/{userId}")
    public String addCartItem(@PathVariable Long userId, @RequestParam("item-id") Long itemId, Model model) {
        try {
            cartService.addCartItem(userId, itemId);
            return "redirect:/cart";
        } catch (Exception e) {
            return "redirect:/items?error=true";
        }
    }
    @GetMapping("/cart")
    public String displayCart(Model model, Authentication authentication) {
        String username = authentication.getName();
        Cart cart = cartService.getCartByUserId(username);
        model.addAttribute("cart", cart != null ? cart : new Cart());
        return "cart";
    }

    @GetMapping("/delete/{itemId}")
    public String removeFromCart(Model model, @PathVariable Long itemId, Authentication authentication) {
        try {
            String username = authentication.getName();
            cartService.removeCartItem(username, itemId);
            return "redirect:/cart";
        } catch (Exception e) {
            return "redirect:/cart?error=true";
        }
    }
}
