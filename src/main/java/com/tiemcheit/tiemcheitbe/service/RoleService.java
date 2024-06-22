package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.RoleRequest;
import com.tiemcheit.tiemcheitbe.dto.request.RoleUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.RoleResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.RoleMapper;
import com.tiemcheit.tiemcheitbe.model.Permission;
import com.tiemcheit.tiemcheitbe.repository.PermissionRepo;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleService {
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final RoleMapper roleMapper;

    public RoleResponse createRole(RoleRequest roleRequest) {
        if (roleRepo.existsByName(roleRequest.getName())) {
            throw new AppException("Role " + roleRequest.getName() + " already exists", HttpStatus.BAD_REQUEST);
        }

        var role = roleMapper.toRole(roleRequest);

        var permissions = permissionRepo.findAllByNameIn(roleRequest.getPermissions());

        role.setPermissions(new HashSet<>(permissions));

        role = roleRepo.save(role);
        return roleMapper.toRoleResponse(role);
    }

    public Set<RoleResponse> getRoles() {
        return roleRepo.findAll().stream().map(roleMapper::toRoleResponse).collect(Collectors.toSet());
    }

    @Transactional
    public void deleteRole(String role) {
        var roleToDelete = roleRepo.findByName(role)
                .orElseThrow(() -> new AppException("Role " + role + " not found", HttpStatus.NOT_FOUND));

        roleToDelete.getPermissions().clear();
        roleToDelete.getUsers().forEach(user -> user.getRoles().remove(roleToDelete));

        roleRepo.save(roleToDelete); // Updates the association table by removing entries
        roleRepo.delete(roleToDelete);
    }

    public RoleResponse updateRole(RoleUpdateRequest request, String roleName) {
        var roleToUpdate = roleRepo.findByName(roleName)
                .orElseThrow(() -> new AppException("Role " + roleName + " not found", HttpStatus.NOT_FOUND));

        Set<Permission> foundPermissions = new HashSet<>();

        for (String permName : request.getPermissions()) {
            Permission perm = permissionRepo.findByName(permName)
                    .orElseThrow(() -> new AppException("Permission " + permName + " not found", HttpStatus.NOT_FOUND));
            foundPermissions.add(perm);
        }

        roleToUpdate.setPermissions(foundPermissions);
        roleToUpdate.setDescription(request.getDescription());

        return roleMapper.toRoleResponse(roleRepo.save(roleToUpdate));
    }

    public RoleResponse getRole(String role) {
        return roleRepo.findByName(role)
                .map(roleMapper::toRoleResponse)
                .orElseThrow(() -> new AppException("Role " + role + " not found", HttpStatus.NOT_FOUND));
    }
}
