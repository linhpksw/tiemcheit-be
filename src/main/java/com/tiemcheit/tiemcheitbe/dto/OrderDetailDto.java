package com.tiemcheit.tiemcheitbe.dto;

import lombok.Data;

@Data
public class OrderDetailDto {
    private Long id;
    private double price;
    private int quantity;
    private OrderDto order;
    private ProductDto product;
}
