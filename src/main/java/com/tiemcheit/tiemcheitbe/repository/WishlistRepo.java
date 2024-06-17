package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.WishlistItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface WishlistRepo extends JpaRepository<WishlistItem, Long> {

    @Modifying
    @Transactional
    @Query("DELETE FROM WishlistItem c WHERE c.user.username = :userName AND c.product.id = :productId")
    void deleteByUserNameAndProductId(@Param("userName") String userName, @Param("productId") Long productId);
}
