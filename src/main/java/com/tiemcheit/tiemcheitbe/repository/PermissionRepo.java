package com.tiemcheit.tiemcheitbe.repository;

import com.tiemcheit.tiemcheitbe.model.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface PermissionRepo extends JpaRepository<Permission, String> {
    Set<Permission> findAllByNameIn(Collection<String> name);

    @Transactional
    void deleteByName(String permissionName);

    boolean existsByName(String permissionName);

    Optional<Permission> findByName(String permissionName);
}
