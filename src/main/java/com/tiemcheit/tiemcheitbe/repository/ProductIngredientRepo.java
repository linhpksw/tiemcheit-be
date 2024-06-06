package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.ProductIngredient;
import com.tiemcheit.tiemcheitbe.model.compositeId.ProductIngredientId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductIngredientRepo extends JpaRepository<ProductIngredient, ProductIngredientId> {
    List<ProductIngredient> findAllByProductId(Long product_id);

    void deleteAllByProductId(Long product_id);

}
