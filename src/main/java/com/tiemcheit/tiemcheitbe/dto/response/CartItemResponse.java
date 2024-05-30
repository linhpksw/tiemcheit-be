package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private int id;
    private UserResponse user;
    private ProductResponse product;
    private int quantity;
}
