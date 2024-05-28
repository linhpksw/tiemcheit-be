package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class CartProductResponse {
    private Long id;
    private String name;
    private int price;
    private String image;
}
