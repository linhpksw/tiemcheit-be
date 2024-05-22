package com.tiemcheit.tiemcheitbe.dto;

import lombok.Data;

@Data
public class ProductDto {
    private Integer id;
    private String name;
    private String image;
    private double price;
    private String description;
}
