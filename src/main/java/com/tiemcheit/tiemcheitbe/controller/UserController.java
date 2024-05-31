package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.UserRegisterRequest;
import com.tiemcheit.tiemcheitbe.dto.request.UserUpdateRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserResponse;
import com.tiemcheit.tiemcheitbe.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;

    @PostMapping("/register")
    ApiResponse<UserResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return ApiResponse.<UserResponse>builder()
                .message("Success")
                .data(userService.registerUser(request))
                .build();
    }

    @GetMapping("/all")
    ApiResponse<List<UserResponse>> getUsers() {
        return ApiResponse.<List<UserResponse>>builder()
                .data(userService.getUsers())
                .build();
    }

    @GetMapping("/{username}")
    ApiResponse<UserResponse> getUser(@PathVariable("username") String username) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.getUser(username))
                .build();
    }

    @GetMapping("/{username}/detail")
    ApiResponse<UserDetailResponse> getUserDetail(@PathVariable("username") String username) {
        return ApiResponse.<UserDetailResponse>builder()
                .data(userService.getUserDetail(username))
                .build();
    }


    @PatchMapping("/{username}")
    ApiResponse<UserResponse> updateUser(@PathVariable String username, @RequestBody @Valid UserUpdateRequest request) {
        return ApiResponse.<UserResponse>builder()
                .data(userService.updateUser(username, request))
                .build();
    }

    @DeleteMapping("/{username}")
    ApiResponse<String> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return ApiResponse.<String>builder()
                .message("User has been deleted")
                .build();
    }
}
