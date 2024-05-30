package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private String image;
    private double price;
    private String description;
}
