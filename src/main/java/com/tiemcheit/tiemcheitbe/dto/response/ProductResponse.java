package com.tiemcheit.tiemcheitbe.dto.response;

import com.tiemcheit.tiemcheitbe.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Double price;
    private String image;
    private int quantity;
    private Category category;
}