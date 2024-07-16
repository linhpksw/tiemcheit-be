package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepo extends JpaRepository<Category, Long> {
    List<Category> findAllByStatus(String status);

    @Query("SELECT c from Category c WHERE c.status = 'active'")
    List<Category> findAllByActiveStatus();

    List<Category> findByName(String name);
}
