package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.model.Category;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private List<String> imageList;
    private double price;
    private Category category;
    private int quantity;
    private String description;
    private Set<Long> optionId;
    private Set<Long> ingredientId;
}
