package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class ProductResponse {
    private Integer id;
    private String name;
    private String image;
    private double price;
    private String description;
}
