package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAddressRepo extends JpaRepository<UserAddress, Long> {
    List<UserAddress> findByUserId(Long userId);

    boolean existsByAddressAndUserId(String address, Long user_id);

    @Modifying
    @Query("DELETE FROM UserAddress ua WHERE ua.id = :id AND ua.user.id = :userId")
    void deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
    
    List<UserAddress> findAllByUserId(Long id);
}
