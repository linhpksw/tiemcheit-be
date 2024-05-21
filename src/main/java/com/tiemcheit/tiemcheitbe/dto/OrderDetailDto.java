package com.tiemcheit.tiemcheitbe.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OrderDetailDto {
    private double price;
    private int quantity;
    private Long orderId;
    private Long productId;
}
