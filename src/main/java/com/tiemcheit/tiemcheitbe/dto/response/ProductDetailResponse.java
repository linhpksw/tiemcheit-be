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
    private String image1;
    private String image2;
    private String image3;
    private double price;
    private int quantity;
    private Category category;
    private List<OptionResponse> optionList;
    private List<IngredientResponse> ingredientList;
}
