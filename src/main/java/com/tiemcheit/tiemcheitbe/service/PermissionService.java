package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.PermissionRequest;
import com.tiemcheit.tiemcheitbe.dto.response.PermissionResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.PermissionMapper;
import com.tiemcheit.tiemcheitbe.model.Permission;
import com.tiemcheit.tiemcheitbe.repository.PermissionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PermissionService {
    private final PermissionRepo permissionRepo;
    private final PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        if (permissionRepo.existsByName(request.getName())) {
            throw new AppException(STR."Permission \{request.getName()} already exists", HttpStatus.BAD_REQUEST);
        }

        Permission permission = permissionMapper.toPermission(request);
        permission = permissionRepo.save(permission);
        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAll() {
        var permissions = permissionRepo.findAll();
        return permissions.stream().map(permissionMapper::toPermissionResponse).toList();
    }

    public void deleteByName(String permission) {
        if (!permissionRepo.existsByName(permission)) {
            throw new AppException(STR."Permission \{permission} not exists", HttpStatus.BAD_REQUEST);
        }

        permissionRepo.deleteByName(permission);
    }
}
