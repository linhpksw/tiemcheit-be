package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private Double price;
    private String image;
}
