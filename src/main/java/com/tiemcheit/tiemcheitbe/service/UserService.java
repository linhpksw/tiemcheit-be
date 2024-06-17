package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.RoleRequest;
import com.tiemcheit.tiemcheitbe.dto.request.UserUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserInfoResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.Permission;
import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.model.UserAddress;
import com.tiemcheit.tiemcheitbe.repository.PermissionRepo;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final PermissionRepo permissionRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public UserInfoResponse getUserInfo(String username) {
        return userMapper.toUserInfoResponse(
                userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND)));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserInfoResponse> getUsersInfo() {
        return userRepo.findAll().stream().map(userMapper::toUserInfoResponse).toList();
    }


    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public UserProfileResponse updateUserProfile(String username, UserUpdateRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFullname() != null) {
            user.setFullname(request.getFullname());
        }

        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        if (request.getGender() != null) {
            user.setGender(request.getGender());
        }

        if (request.getStatus() != null) {
            user.setStatus(request.getStatus());
        }

        if (request.getIsActivated() != null) {
            user.setIsActivated(request.getIsActivated());
        }

        if (request.getRoles() != null) {
            user.getRoles().clear();

            for (RoleRequest roleRequest : request.getRoles()) {
                Role role = roleRepo.findByName(roleRequest.getName()).orElseThrow(() -> new AppException("Role not found.", HttpStatus.NOT_FOUND));

                Set<Permission> permissions = new HashSet<>();
                for (String permName : roleRequest.getPermissions()) {
                    Permission permission = permissionRepo.findByName(permName)
                            .orElseThrow(() -> new AppException("Permission not found.", HttpStatus.NOT_FOUND));
                    permissions.add(permission);
                }
                role.setPermissions(permissions);

                user.getRoles().add(role);
            }
        }

        if (request.getAddresses() != null) {
            user.getAddresses().clear();

            // Build and add new addresses to the existing collection
            request.getAddresses().forEach(addr -> {
                user.getAddresses().add(
                        UserAddress.builder()
                                .address(addr.getAddress())
                                .isDefault(addr.getIsDefault())
                                .user(user).build());
            });
        }

        User savedUser = userRepo.save(user);

        return userMapper.toUserProfileResponse(savedUser);
    }

    @PreAuthorize("#username == authentication.name || hasRole('ROLE_ADMIN')")
    public UserProfileResponse getUserProfile(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        return userMapper.toUserProfileResponse(user);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<UserProfileResponse> getUsersProfile() {
        return userRepo.findAll().stream().map(userMapper::toUserProfileResponse).toList();
    }
}
