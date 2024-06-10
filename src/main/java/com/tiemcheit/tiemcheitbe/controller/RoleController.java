package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.RoleRequest;
import com.tiemcheit.tiemcheitbe.dto.request.RoleUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.RoleResponse;
import com.tiemcheit.tiemcheitbe.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/roles")
public class RoleController {
    private final RoleService roleService;

    @PostMapping
    ApiResponse<RoleResponse> createRole(@RequestBody RoleRequest request) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.createRole(request))
                .message("Success")
                .build();
    }

    @GetMapping("/{roleName}")
    ApiResponse<RoleResponse> getRole(@PathVariable String roleName) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.getRole(roleName))
                .message("Success")
                .build();
    }

    @PutMapping("/{roleName}")
    ApiResponse<RoleResponse> updateRole(@RequestBody RoleUpdateRequest request, @PathVariable String roleName) {
        return ApiResponse.<RoleResponse>builder()
                .data(roleService.updateRole(request, roleName))
                .message("Success")
                .build();
    }

    @DeleteMapping("/{roleName}")
    ApiResponse<Void> deleteRole(@PathVariable String roleName) {
        roleService.deleteRole(roleName);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @GetMapping
    ApiResponse<Set<RoleResponse>> getRoles() {
        return ApiResponse.<Set<RoleResponse>>builder()
                .data(roleService.getRoles())
                .message("Success")
                .build();
    }


}
