package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.username = :credential OR u.email = :credential OR u.phone = :credential")
    Optional<User> findByCredential(@Param("credential") String credential);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findUsersByRole(@Param("roleName") String roleName);

    boolean existsByUsername(String username);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'CUSTOMER' " +
            "AND (:status IS NULL OR u.status = :status) ORDER BY u.createdAt ASC")
    List<User> findAscCustomersOrderByCreatedAt(@Param("status") String status);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'CUSTOMER' " +
            "AND (:status IS NULL OR u.status = :status) ORDER BY u.createdAt DESC")
    List<User> findDescCustomersOrderByCreatedAt(@Param("status") String status);

    @Query("SELECT u FROM User u JOIN u.roles r LEFT JOIN u.orders o WHERE r.name = 'CUSTOMER' " +
            "AND (:status IS NULL OR u.status = :status) GROUP BY u.id ORDER BY COUNT(o) ASC")
    List<User> findAscCustomersOrderByOrderNumber(@Param("status") String status);

    @Query("SELECT u FROM User u JOIN u.roles r LEFT JOIN u.orders o WHERE r.name = 'CUSTOMER' " +
            "AND (:status IS NULL OR u.status = :status) GROUP BY u.id ORDER BY COUNT(o) DESC")
    List<User> findDescCustomersOrderByOrderNumber(@Param("status") String status);

    @Query("SELECT u, SUM(od.price * od.quantity) AS totalSpent " +
            "FROM User u " +
            "JOIN u.roles r " +
            "LEFT JOIN u.orders o " +
            "LEFT JOIN o.orderDetails od " +
            "WHERE r.name = 'CUSTOMER' AND (:status IS NULL OR u.status = :status) " +
            "GROUP BY u.id " +
            "ORDER BY totalSpent ASC")
    List<User> findAscCustomersOrderByOrderTotal(@Param("status") String status);

    @Query("SELECT u, SUM(od.price * od.quantity) AS totalSpent " +
            "FROM User u " +
            "JOIN u.roles r " +
            "LEFT JOIN u.orders o " +
            "LEFT JOIN o.orderDetails od " +
            "WHERE r.name = 'CUSTOMER' AND (:status IS NULL OR u.status = :status) " +
            "GROUP BY u.id " +
            "ORDER BY totalSpent DESC")
    List<User> findDescCustomersOrderByOrderTotal(@Param("status") String status);

    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = 'CUSTOMER' " +
            "AND (:status IS NULL OR u.status = :status)")
    List<User> findCustomerByStatus(String status);
}
