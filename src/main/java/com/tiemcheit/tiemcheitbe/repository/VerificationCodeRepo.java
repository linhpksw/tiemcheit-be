package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface VerificationCodeRepo extends JpaRepository<VerificationCode, Long> {
    // Custom method to delete verification codes by user
    @Modifying
    @Transactional
    @Query("DELETE FROM VerificationCode vc WHERE vc.user.id = :userId")
    void deleteByUserId(Long userId);
}
