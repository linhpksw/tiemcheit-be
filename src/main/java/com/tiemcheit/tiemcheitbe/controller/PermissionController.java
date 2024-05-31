package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.PermissionRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.PermissionResponse;
import com.tiemcheit.tiemcheitbe.service.PermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
@RequiredArgsConstructor
@Slf4j
public class PermissionController {
    private final PermissionService permissionService;

    @PostMapping
    ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.create(request))
                .message("Success")
                .build();
    }

    @GetMapping("/{permission}")
    ApiResponse<PermissionResponse> getByName(@PathVariable String permission) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.getByName(permission))
                .message("Success")
                .build();
    }

    @PutMapping
    ApiResponse<PermissionResponse> update(@RequestBody PermissionRequest request) {
        return ApiResponse.<PermissionResponse>builder()
                .data(permissionService.update(request))
                .message("Success")
                .build();
    }

    @GetMapping("/all")
    ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse.<List<PermissionResponse>>builder()
                .data(permissionService.getAll())
                .message("Success")
                .build();
    }

    @DeleteMapping("/{permission}")
    ApiResponse<Void> delete(@PathVariable String permission) {
        permissionService.deleteByName(permission);
        return ApiResponse.<Void>builder().message("Success").build();
    }
}
