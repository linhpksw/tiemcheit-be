package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class OrderDetailResponse {
    private Long id;
    private double price;
    private int quantity;
    private OrderResponse order;
    private ProductResponse product;
}
