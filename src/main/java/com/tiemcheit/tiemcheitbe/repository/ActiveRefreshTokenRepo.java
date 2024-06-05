package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.ActiveRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface ActiveRefreshTokenRepo extends JpaRepository<ActiveRefreshToken, Long> {
    Optional<ActiveRefreshToken> findByUser_Username(String username);

    Optional<ActiveRefreshToken> findByJti(String jti);

    @Transactional
    void deleteByUser_Username(String username);

    @Transactional
    void deleteByJti(String jti);
}
