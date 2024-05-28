package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    List<Product> findAllByCategoryId(Long category_id);
}
