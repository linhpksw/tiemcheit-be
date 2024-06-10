package com.tiemcheit.tiemcheitbe.controller;

import com.nimbusds.jose.JOSEException;
import com.tiemcheit.tiemcheitbe.dto.request.*;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.AuthResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserInfoResponse;
import com.tiemcheit.tiemcheitbe.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    ApiResponse<UserInfoResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        return ApiResponse.<UserInfoResponse>builder()
                .message("Success")
                .data(authService.register(request))
                .build();
    }

    @PostMapping("/login")
    ApiResponse<AuthResponse> login(@RequestBody AuthRequest request) throws ParseException {
        var data = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder().message("Success").data(data).build();
    }

    @PostMapping("/introspect")
    ApiResponse<Void> introspect(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        authService.introspect(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> refreshToken(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var data = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder().data(data).message("Success").build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/reset-password")
    ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/deactivate")
    ApiResponse<Void> deactivate(@RequestBody DeactivateRequest request) throws ParseException, JOSEException {
        authService.deactivate(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }
}
