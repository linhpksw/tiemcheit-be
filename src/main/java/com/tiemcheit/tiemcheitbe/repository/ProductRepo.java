package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {

//    @Query("SELECT p FROM Product p WHERE p.id = :productId")
//    Product findProductById(@Param("productId") Long productId);

    @Query("SELECT p FROM Product p WHERE p.category.id = :category_id AND (p.status = 'active' OR p.status = 'disabled')")
    List<Product> findAllByCategoryId(Long category_id);

    //    @Query("SELECT p FROM Product p WHERE p.status = 'active' OR p.status = 'disabled'")
    List<Product> findAll(Specification<Product> specification, Sort sort);

    @Query("SELECT  p FROM Product p WHERE p.status = 'active' ORDER BY p.sold DESC")
    List<Product> findTopBestsellers(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE p.status = 'active' OR p.status = 'disabled'")
    List<Product> findAllByActiveAndDisabledStatus();

    @Query("SELECT p FROM Product p WHERE p.status = 'custom' AND p.user.id = :userId")
    List<Product> findAllCreatedProductsOfUser(Long userId);

    List<Product> findAllByStatus(String status);

    List<Product> findAllByCategoryIdAndStatus(Long category_id, String status);

    @Query("SELECT p " +
            "FROM Product p " +
            "JOIN p.productIngredients pi " +
            "JOIN pi.ingredient i " +
            "WHERE p.category.id = :categoryId " +
            "AND p.id != :productId " +
            "AND (p.status = 'active' OR p.status = 'disabled') " +
            "AND pi.unit < i.quantity " +
            "GROUP BY p.id " +
            "HAVING COUNT(pi) = (SELECT COUNT(pi2) " +
            "                    FROM ProductIngredient pi2 " +
            "                    WHERE pi2.product.id = p.id)")
    List<Product> findAllExceptIdByCategoryId(Long productId, Long categoryId);

}
