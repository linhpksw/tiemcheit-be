package com.tiemcheit.tiemcheitbe.dto;

import lombok.Data;

@Data
public class GetCartItemDto {
    private int id;
    private UserDto user;
    private ProductDto product;
    private int quantity;

}
