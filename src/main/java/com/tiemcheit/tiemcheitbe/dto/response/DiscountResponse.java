package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class DiscountResponse {
    private Long id;
    private String type; // category, product, total, ship
    private Long categoryId;
    private Long productId;
    private String valueType; // percent, fixed
    private Double valueFixed;
}
