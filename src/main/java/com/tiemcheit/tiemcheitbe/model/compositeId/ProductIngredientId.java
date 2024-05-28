package com.tiemcheit.tiemcheitbe.model.compositeId;

import com.tiemcheit.tiemcheitbe.model.Ingredient;
import com.tiemcheit.tiemcheitbe.model.Product;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.io.Serializable;

public class ProductIngredientId implements Serializable {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    private Ingredient ingredient;
}
