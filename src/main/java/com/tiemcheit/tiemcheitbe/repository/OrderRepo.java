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
    List<Order> findAllByUserOrderByIdDesc(User user);

    List<Order> findAllByUser(User user);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate")
    List<Order> findAllByOrderDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status")
    List<Order> findAllByOrderStatus(@Param("status") String status);

    @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.orderStatus = :status")
    List<Order> findAllByOrderDateBetweenAndOrderStatus(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status);

    // Method to count orders by user ID
    long countByUser_Id(@Param("userId") Long userId);

    // Method to get total amount spent by user ID
    @Query("SELECT SUM(od.price * od.quantity) FROM Order o JOIN o.orderDetails od WHERE o.user.id = :userId")
    Double getTotalAmountSpentByUser(@Param("userId") Long userId);
}
