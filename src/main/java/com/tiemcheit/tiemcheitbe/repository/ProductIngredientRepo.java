package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.ProductIngredient;
import com.tiemcheit.tiemcheitbe.model.compositeId.ProductIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductIngredientRepo extends JpaRepository<ProductIngredient, ProductIngredientId> {
    List<ProductIngredient> findAllByProductId(Long product_id);
}
