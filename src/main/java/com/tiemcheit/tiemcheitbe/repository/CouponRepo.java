package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CouponRepo extends JpaRepository<Coupon, Long> {
    @Query("SELECT c FROM Coupon c ORDER BY c.id")
    List<Coupon> findAllCoupon();

    List<Coupon> findAllById(Iterable<Long> ids);
    
    Coupon findByCode(String code);
}
