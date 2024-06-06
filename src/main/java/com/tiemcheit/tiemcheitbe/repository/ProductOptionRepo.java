package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.ProductOption;
import com.tiemcheit.tiemcheitbe.model.compositeId.ProductOptionId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductOptionRepo extends JpaRepository<ProductOption, ProductOptionId> {
    List<ProductOption> findAllByProductId(Long product_id);

    void deleteAllByProductId(Long product_id);

}
