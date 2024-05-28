package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private UserResponse user;
    private ProductResponse product;
    private int quantity;
}
