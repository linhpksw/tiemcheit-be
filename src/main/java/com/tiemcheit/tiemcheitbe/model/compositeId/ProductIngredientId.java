package com.tiemcheit.tiemcheitbe.model.compositeId;

import com.tiemcheit.tiemcheitbe.model.Ingredient;
import com.tiemcheit.tiemcheitbe.model.Product;
import jakarta.persistence.Id;

import java.io.Serializable;

public class ProductIngredientId implements Serializable {
    @Id
    private Product product;

    @Id
    private Ingredient ingredient;
}
