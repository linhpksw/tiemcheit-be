package com.tiemcheit.tiemcheitbe.model;

import com.tiemcheit.tiemcheitbe.model.compositeId.ProductIngredientId;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products_ingredients")
@IdClass(ProductIngredientId.class)
public class ProductIngredient {
    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_id", nullable = false)
    private Ingredient ingredient;

    @Column(name = "unit_in_cup")
    private float unit;

}
