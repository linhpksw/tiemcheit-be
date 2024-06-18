package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountRepo extends JpaRepository<Discount, Long> {
    Discount findByTypeAndCategoryIdAndProductIdAndValueTypeAndValueFixed(
            String type,
            Long categoryId,
            Long productId,
            String valueType,
            double valueFixed
    );
}
