package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.UserRegisterRequest;
import com.tiemcheit.tiemcheitbe.dto.request.UserUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.UserDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.UserMapper;
import com.tiemcheit.tiemcheitbe.model.Role;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.model.UserAddress;
import com.tiemcheit.tiemcheitbe.repository.RoleRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j

public class UserService {
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepo roleRepo;
    private final UserMapper userMapper;

    @Transactional
    public UserResponse registerUser(UserRegisterRequest request) {
        if (userRepo.existsByUsername(request.getUsername())) {
            throw new AppException("User already exists with this username.", HttpStatus.BAD_REQUEST);
        }

        User user = UserMapper.INSTANCE.toUser(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Role> roles = new HashSet<>();
        roleRepo.findByName("CUSTOMER").ifPresent(roles::add);

        user.setRoles(roles);


        User finalUser = user;
        Set<UserAddress> addresses = request.getAddresses().stream()
                .map(addr -> UserAddress.builder()
                        .address(addr.getAddress())
                        .isDefault(addr.getIsDefault())
                        .user(finalUser)
                        .build())
                .collect(Collectors.toSet());

        user.setAddresses(addresses);

        // Save the user and addresses due to cascade
        user = userRepo.save(user);

        return userMapper.toUserResponse(user);
    }

    @PreAuthorize("#username == authentication.name")
    public UserResponse updateUser(String username, UserUpdateRequest request) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (request.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        if (request.getFullname() != null) {
            user.setFullname(request.getFullname());
        }

        if (request.getDob() != null) {
            user.setDob(request.getDob());
        }

        if (request.getAddresses() != null) {
            user.getAddresses().clear();  // Clear existing addresses

            // Build and add new addresses to the existing collection
            request.getAddresses().forEach(addr -> {
                user.getAddresses().add(
                        UserAddress.builder()
                                .address(addr.getAddress())
                                .isDefault(addr.getIsDefault())
                                .user(user).build());
                // Add to the existing collection
            });
        }

        User savedUser = userRepo.save(user);

        return userMapper.toUserResponse(savedUser);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(String username) {
        userRepo.deleteByUsername(username);
    }

    //    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> getUsers() {
        return userRepo.findAll().stream().map(userMapper::toUserResponse).toList();
    }

    //    @PreAuthorize("#username == authentication.name && hasAuthority('READ_STH')")
    @PreAuthorize("#username == authentication.name")
    public UserResponse getUser(String username) {
        return userMapper.toUserResponse(
                userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND)));
    }


    public User getById(Long id) {
        return userRepo.getReferenceById(id);
    }

    @PreAuthorize("#username == authentication.name")
    public UserDetailResponse getUserDetail(String username) {
        User user = userRepo.findByUsername(username).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        return userMapper.toUserDetailResponse(user);
    }
}
