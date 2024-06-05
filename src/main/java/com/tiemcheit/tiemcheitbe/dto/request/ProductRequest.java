package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.model.Category;
import lombok.Data;

import java.util.Date;
import java.util.Set;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private String image1;
    private String image2;
    private String image3;
    private double price;
    private Category category;
    private int quantity;
    private Date createAt;
    private String description;
    private Set<Long> optionId;
    private Set<Long> ingredientId;
}
