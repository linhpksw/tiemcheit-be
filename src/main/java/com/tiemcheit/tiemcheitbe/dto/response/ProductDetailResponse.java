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
public class ProductDetailResponse {
    private Long id;
    private String name;
    private String description;
    private List<String> imageList;
    private Double price;
    private Integer quantity;
    private Category category;
    private List<OptionResponse> optionList;
    private List<IngredientResponse> ingredientList;
    private String status;
}
