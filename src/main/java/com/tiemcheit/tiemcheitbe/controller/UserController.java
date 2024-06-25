package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.UserAddAddressRequest;
import com.tiemcheit.tiemcheitbe.dto.request.UserUpdateAddressRequest;
import com.tiemcheit.tiemcheitbe.dto.request.UserUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserAddAddressResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserInfoResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserProfileResponse;
import com.tiemcheit.tiemcheitbe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @GetMapping("/users/{username}")
    ApiResponse<UserInfoResponse> getUserInfo(@PathVariable String username) {
        return ApiResponse.<UserInfoResponse>builder()
                .data(userService.getUserInfo(username))
                .message("Success")
                .build();
    }

    @GetMapping("/users")
    ApiResponse<List<UserInfoResponse>> getUsersInfo() {
        return ApiResponse.<List<UserInfoResponse>>builder()
                .data(userService.getUsersInfo())
                .message("Success")
                .build();
    }

    @PatchMapping("/users/{username}/profile")
    ApiResponse<UserProfileResponse> updateUserProfile(@PathVariable String username, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .data(userService.updateUserProfile(username, request))
                .build();
    }

    @GetMapping("/users/{username}/profile")
    ApiResponse<UserProfileResponse> getUserProfile(@PathVariable("username") String username) {
        return ApiResponse.<UserProfileResponse>builder()
                .data(userService.getUserProfile(username))
                .build();
    }

    @GetMapping("/users/profile")
    ApiResponse<List<UserProfileResponse>> getUsersProfile() {
        return ApiResponse.<List<UserProfileResponse>>builder()
                .data(userService.getUsersProfile())
                .build();
    }

    @GetMapping("/{username}")
    ApiResponse<UserInfoResponse> getInfo(@PathVariable String username) {
        return ApiResponse.<UserInfoResponse>builder()
                .data(userService.getUserInfo(username))
                .build();
    }

    @GetMapping("/{username}/profile")
    ApiResponse<UserProfileResponse> getProfile(@PathVariable String username) {
        return ApiResponse.<UserProfileResponse>builder()
                .data(userService.getUserProfile(username))
                .build();
    }

    @PatchMapping("/{username}/profile")
    ApiResponse<UserProfileResponse> updateProfile(@PathVariable String username, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserProfileResponse>builder()
                .data(userService.updateUserProfile(username, request))
                .build();
    }

    @PatchMapping("/{username}/addresses/{addressId}")
    ApiResponse<Void> updateUserAddress(@PathVariable String username, @PathVariable Long addressId, @RequestBody @Valid UserUpdateAddressRequest request) {
        userService.updateUserAddress(username, addressId, request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/{username}/addresses")
    ApiResponse<UserAddAddressResponse> addUserAddress(@PathVariable String username, @RequestBody @Valid UserAddAddressRequest request) {
        return ApiResponse.<UserAddAddressResponse>builder().data(userService.addUserAddress(username, request)).message("Success").build();
    }

    @DeleteMapping("/{username}/addresses/{addressId}")
    ApiResponse<Void> deleteUserAddress(@PathVariable String username, @PathVariable Long addressId) {
        userService.deleteUserAddress(username, addressId);
        return ApiResponse.<Void>builder().message("Success").build();
    }
}
