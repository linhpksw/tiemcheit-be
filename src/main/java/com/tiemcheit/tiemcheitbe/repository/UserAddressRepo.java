package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserAddressRepo extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserId(Long userId);
}
