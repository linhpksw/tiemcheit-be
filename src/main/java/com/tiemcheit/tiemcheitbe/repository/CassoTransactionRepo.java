package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.CassoTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CassoTransactionRepo extends JpaRepository<CassoTransaction, Long> {
    @Query("SELECT c FROM CassoTransaction c WHERE c.description LIKE %:username% ORDER BY c.id DESC LIMIT 1")
    Optional<CassoTransaction> findTop1ByUsernameFromDescription(String username);

    @Transactional
    @Modifying
    @Query("DELETE FROM CassoTransaction c WHERE c.description LIKE %:username%")
    void deleteByUsername(String username);
}
