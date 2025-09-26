package com.crus.client_application.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Item {

    @JsonProperty("itemId")
    private Long id;
    private String name;
    private String description;
}
