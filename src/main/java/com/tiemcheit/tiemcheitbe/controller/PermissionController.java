package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.PermissionRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.PermissionResponse;
import com.tiemcheit.tiemcheitbe.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/permissions")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> createPermission(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.createPermission(request))
                .message("Success")
                .build();
    }

    @GetMapping("/{permissionName}")
    ApiResponse<PermissionResponse> getPermission(@PathVariable String permissionName) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.getPermission(permissionName))
                .message("Success")
                .build();
    }

    @PutMapping("/{permissionName}")
    ApiResponse<PermissionResponse> updatePermission(@RequestBody PermissionRequest request, @PathVariable String permissionName) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.updatePermission(request, permissionName))
                .message("Success")
                .build();
    }


    @DeleteMapping("/{permissionName}")
    ApiResponse<Void> deletePermission(@PathVariable String permissionName) {
        permissionService.deletePermission(permissionName);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @GetMapping
    ApiResponse<Set<PermissionResponse>> getPermissions() {
        return ApiResponse.<Set<PermissionResponse>>builder()
                .data(permissionService.getPermissions())
                .message("Success")
                .build();
    }
}
