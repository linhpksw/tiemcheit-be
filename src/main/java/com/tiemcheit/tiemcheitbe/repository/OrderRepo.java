package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Order;
import com.tiemcheit.tiemcheitbe.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    List<Order> findAllByUserOrderByIdDesc(User user);
}
