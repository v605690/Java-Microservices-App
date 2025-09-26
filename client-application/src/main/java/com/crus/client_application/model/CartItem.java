package com.crus.client_application.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class CartItem {
    private Long id;
    private Long itemId;
    private Integer amount;
    private Item item;
}
