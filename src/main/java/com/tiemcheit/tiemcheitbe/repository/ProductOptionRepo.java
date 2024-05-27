package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.ProductOption;
import com.tiemcheit.tiemcheitbe.model.compositeId.ProductOptionId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductOptionRepo extends JpaRepository<ProductOption,ProductOptionId>
{
    List<ProductOption> findAllByProductId(Long product_id);
}
