package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentRepo extends JpaRepository<Payment, Long> {
    Optional<Payment> findTop1ByUsernameOrderByOrderDateDesc(String username);

    @Query(value = "SELECT * FROM payments p WHERE p.username = :username AND p.total_price = :amount ORDER BY p.order_date DESC LIMIT 1", nativeQuery = true)
    Optional<Payment> findMatchingPayment(String username, Long amount);


}
