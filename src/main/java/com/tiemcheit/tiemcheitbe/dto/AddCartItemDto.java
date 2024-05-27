package com.tiemcheit.tiemcheitbe.dto;

import lombok.Data;

@Data
public class AddCartItemDto {
    private UserDto user;
    private ProductDto product;
    private int quantity;
}
