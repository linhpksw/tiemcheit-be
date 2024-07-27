package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.model.ProductIngredient;
import com.tiemcheit.tiemcheitbe.model.compositeId.ProductIngredientId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductIngredientRepo extends JpaRepository<ProductIngredient, ProductIngredientId> {
    @Query("SELECT pi FROM ProductIngredient pi WHERE pi.product.id = :product_id")
    List<ProductIngredient> findAllByProductId(Long product_id);

    List<ProductIngredient> findAllByIngredientId(Long ingredient_id);
    @Query("SELECT DISTINCT pi.product FROM ProductIngredient pi WHERE pi.unit > pi.ingredient.quantity AND pi.product.status <> 'custom' AND pi.ingredient.status <> 'disabled'")
    Page<Product> findDistinctProductsByUnitInCupGreaterThanIngredientQuantity(Pageable pageable);
    void deleteAllByProductId(Long product_id);

}
