package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class CartItemRequest {
    private ProductRequest product;
    private int quantity;
}
