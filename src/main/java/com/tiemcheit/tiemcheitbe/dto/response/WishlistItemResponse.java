package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class WishlistItemResponse {
    private int id;
    private UserResponse user;
    private ProductResponse product;
}
