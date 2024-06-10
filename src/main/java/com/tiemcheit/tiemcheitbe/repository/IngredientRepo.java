package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRepo extends JpaRepository<Ingredient, Long> {
}
