package com.crus.client_application.model;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Cart {
    private Long id;
    private Long userId;

    private List<CartItem> items = new ArrayList<>();

}
