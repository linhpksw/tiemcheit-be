package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Order;
import com.tiemcheit.tiemcheitbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllByUserOrderDateDesc();

    List<Order> findAllByUserOrderByIdDesc(User user);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (:startDate IS NULL OR o.orderDate >= :startDate) AND (:endDate IS NULL OR o.orderDate <= :endDate) AND (:status IS NULL OR o.orderStatus = :status) ORDER BY o.orderDate DESC")
    List<Order> findAllByUserIdAndOptionalFilters(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status);

    @Query("SELECT o FROM Order o WHERE (:startDate IS NULL OR o.orderDate >= :startDate) AND (:endDate IS NULL OR o.orderDate <= :endDate) AND (:status IS NULL OR o.orderStatus = :status) ORDER BY o.orderDate DESC")
    List<Order> findAllByOptionalFilters(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status);

}
