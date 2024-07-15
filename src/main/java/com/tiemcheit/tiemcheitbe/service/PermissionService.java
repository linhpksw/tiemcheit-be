package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.PermissionRequest;
import com.tiemcheit.tiemcheitbe.dto.response.PermissionResponse;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.PermissionMapper;
import com.tiemcheit.tiemcheitbe.model.Permission;
import com.tiemcheit.tiemcheitbe.repository.PermissionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    private final PermissionRepo permissionRepo;
    private final PermissionMapper permissionMapper;

    public PermissionResponse createPermission(PermissionRequest request) {
        if (permissionRepo.existsByName(request.getName())) {
            throw new AppException("Permission " + request.getName() + " already exists", HttpStatus.BAD_REQUEST);
        }

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepo.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public Set<PermissionResponse> getPermissions() {
        var permissions = permissionRepo.findAll();
        return permissions.stream()
                .map(permissionMapper::toPermissionResponse)
                .collect(Collectors.toSet());
    }

    public PermissionResponse getPermission(String permission) {
        var permissionEntity = permissionRepo.findByName(permission)
                .orElseThrow(() -> new AppException("Permission " + permission + " not found", HttpStatus.NOT_FOUND));
        return permissionMapper.toPermissionResponse(permissionEntity);
    }

    public void deletePermission(String permission) {
        if (!permissionRepo.existsByName(permission)) {
            throw new AppException("Permission " + permission + " not exists", HttpStatus.BAD_REQUEST);
        }

        permissionRepo.deleteByName(permission);
    }

    public PermissionResponse updatePermission(PermissionRequest request, String permissionName) {
        var permissionToUpdate = permissionRepo.findByName(permissionName)
                .orElseThrow(() -> new AppException("Permission " + permissionName + " not found", HttpStatus.NOT_FOUND));

        permissionRepo.findByName(request.getName())
                .ifPresent(permission -> {
                    if (!permission.getName().equals(permissionName)) {
                        throw new AppException("Permission " + request.getName() + " already exists", HttpStatus.BAD_REQUEST);
                    }
                });

        permissionToUpdate.setName(request.getName());
        permissionToUpdate.setDescription(request.getDescription());

        permissionToUpdate = permissionRepo.save(permissionToUpdate);
        return permissionMapper.toPermissionResponse(permissionToUpdate);
    }


}
