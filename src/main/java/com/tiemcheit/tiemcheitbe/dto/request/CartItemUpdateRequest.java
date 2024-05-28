package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class CartItemUpdateRequest {
    private Long id;
    private int quantity;
}
