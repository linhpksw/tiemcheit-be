package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngredientRestockRequest {
    private String ingredientName;
    private int quantity;
}
