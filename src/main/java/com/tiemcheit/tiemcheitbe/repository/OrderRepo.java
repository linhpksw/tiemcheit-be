package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Order;
import com.tiemcheit.tiemcheitbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o ORDER BY o.orderDate DESC")
    List<Order> findAllByUserOrderDateDesc();

    @Query("SELECT o FROM Order o WHERE o.orderStatus = :status")
    List<Order> findAllByOrderStatus(String status);

    @Query("SELECT o FROM Order o WHERE o.orderStatus <> :status")
    List<Order> findAllByOrderStatusExcept(String status);

    List<Order> findAllByUser(User user);

    List<Order> findAllByUserOrderByIdDesc(User user);

    @Query("SELECT o FROM Order o WHERE o.user.id = :userId AND (:startDate IS NULL OR o.orderDate >= :startDate) AND (:endDate IS NULL OR o.orderDate <= :endDate) AND (:status IS NULL OR o.orderStatus = :status) ORDER BY o.orderDate DESC")
    List<Order> findAllByUserIdAndOptionalFilters(@Param("userId") Long userId, @Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status);

    @Query("SELECT o FROM Order o WHERE (:startDate IS NULL OR cast(o.orderDate as date) >= :startDate) AND (:endDate IS NULL OR cast(o.orderDate as date) <= :endDate) AND (:status IS NULL OR o.orderStatus = :status) ORDER BY o.orderDate DESC")
    List<Order> findAllByOptionalFilters(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status);

    // @Query("SELECT o FROM Order o WHERE o.orderDate BETWEEN :startDate AND :endDate AND o.orderStatus = :status")
    // List<Order> findAllByOrderDateBetweenAndOrderStatus(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("status") String status);

    long countByUser_Id(@Param("userId") Long userId);

    @Query("SELECT SUM(od.price * od.quantity) FROM Order o JOIN o.orderDetails od WHERE o.user.id = :userId")
    Double getTotalAmountSpentByUser(@Param("userId") Long userId);

    List<Order> findByUserIdAndCouponId(Long userId, Long couponId);

    @Query("SELECT MONTH(o.orderDate) AS month, COUNT(o) AS count FROM Order o WHERE o.orderStatus = :status AND YEAR(o.orderDate) = :year GROUP BY MONTH(o.orderDate)")
    List<Object[]> countOrdersByStatusAndMonth(@Param("status") String status, @Param("year") int year);

    @Query(value = "SELECT MONTH(o.order_date) AS month, SUM(p.price * od.quantity) AS total_price " +
            "FROM orders o " +
            "JOIN order_details od ON o.id = od.order_id " +
            "JOIN products p ON od.product_id = p.id " +
            "WHERE o.order_status <> 'Order Canceled' AND YEAR(o.order_date) = :year " +
            "GROUP BY MONTH(o.order_date) " +
            "ORDER BY MONTH(o.order_date)",
            nativeQuery = true)
    List<Object[]> sumOrderRevenueByMonth(@Param("year") int year);

    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.orderStatus = :status WHERE o.id IN :ids")
    int updateOrderStatusByIds(@Param("status") String status, @Param("ids") List<Long> ids);
}
