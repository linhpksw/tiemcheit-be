package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.RoleRequest;
import com.tiemcheit.tiemcheitbe.dto.response.RoleResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.RoleMapper;
import com.tiemcheit.tiemcheitbe.repository.PermissionRepo;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        if (roleRepo.existsByName(request.getName())) {
            throw new AppException(STR."Role \{request.getName()} already exists", HttpStatus.BAD_REQUEST);
        }

        var role = roleMapper.toRole(request);

        var permissions = permissionRepo.findAllByNameIn(request.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepo.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public List<RoleResponse> getAll() {
        return roleRepo.findAll().stream().map(roleMapper::toRoleResponse).toList();
    }

    public void delete(String role) {
        roleRepo.deleteByName(role);
    }

    public RoleResponse update(String role, RoleRequest request) {
        var roleToUpdate = roleRepo.findByName(role)
                .orElseThrow(() -> new AppException(STR."Role \{role} not found", HttpStatus.NOT_FOUND));

        var permissions = permissionRepo.findAllByNameIn(request.getPermissions());

        roleToUpdate.setPermissions(new HashSet<>(permissions));

        roleToUpdate = roleRepo.save(roleToUpdate);
        return roleMapper.toRoleResponse(roleToUpdate);
    }
}
