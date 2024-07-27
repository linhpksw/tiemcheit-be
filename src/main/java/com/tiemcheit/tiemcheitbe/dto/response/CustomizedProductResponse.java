package com.tiemcheit.tiemcheitbe.dto.response;

import com.tiemcheit.tiemcheitbe.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomizedProductResponse {
    private Long id;
    private String name;
    private Double price;
    private Integer quantity;
    private List<ProductIngredientResponse> ingredientList;
    private String status;
}
