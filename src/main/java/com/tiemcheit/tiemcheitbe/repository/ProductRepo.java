package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.status = 'active' OR p.status = 'disabled'")
    List<Product> findAllByCategoryId(Long category_id);

    @Query("SELECT p FROM Product p WHERE p.status = 'active' OR p.status = 'disabled'")
    List<Product> findAll(Specification<Product> specification, Sort sort);

    @Query("SELECT  p FROM Product p WHERE p.status = 'active' ORDER BY p.sold DESC")
    List<Product> findTopBestsellers(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 'active' OR p.status = 'disabled'")
    List<Product> findAllByActiveAndDisabledStatus();

    List<Product> findAllByStatus(String status);
}
