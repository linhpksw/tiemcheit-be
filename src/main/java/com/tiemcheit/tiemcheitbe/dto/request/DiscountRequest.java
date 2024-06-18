package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class DiscountRequest {
    private String type; // category, product, total, ship
    private Long categoryId;
    private Long productId;
    private String valueType; // percent, fixed
    private Double valueFixed;
}
