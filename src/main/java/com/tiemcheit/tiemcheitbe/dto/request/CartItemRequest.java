package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserResponse;
import lombok.Data;

@Data
public class CartItemRequest {
    private UserResponse user;
    private ProductResponse product;
    private int quantity;
}
