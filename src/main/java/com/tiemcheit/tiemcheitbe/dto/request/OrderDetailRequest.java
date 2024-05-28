package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import lombok.Data;

@Data
public class OrderDetailRequest {
    private Long id;
    private double price;
    private int quantity;
    private OrderResponse order;
    private ProductResponse product;
}
