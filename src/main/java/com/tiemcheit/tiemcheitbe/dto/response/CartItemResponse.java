package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class CartItemResponse {
    private int id;
    private CartUserResponse user;
    private CartProductResponse product;
    private int quantity;
}
