package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findTop1ByUsernameOrderByOrderDateDesc(String username);

    @Query("SELECT p FROM Payment p WHERE p.username = :username AND p.totalPrice = :amount ORDER BY p.orderDate DESC")
    Optional<Payment> findTopByUsernameAndTotalPriceOrderByOrderDateDesc(String username, Long amount);


}
