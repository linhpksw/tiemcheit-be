package com.tiemcheit.tiemcheitbe.controller;

import com.nimbusds.jose.JOSEException;
import com.tiemcheit.tiemcheitbe.dto.request.*;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.AuthResponse;
import com.tiemcheit.tiemcheitbe.dto.response.UserInfoResponse;
import com.tiemcheit.tiemcheitbe.service.AuthService;
import com.tiemcheit.tiemcheitbe.service.VerificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final VerificationService verificationService;

    @PostMapping("/send-forgot-code")
    ApiResponse<Void> sendForgotCode(@RequestBody @Valid ForgotPasswordRequest request) {
        authService.sendForgotCode(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/oauth2")
    ApiResponse<AuthResponse> oauth2(@RequestParam("code") String code) throws ParseException {
        var data = authService.oauth2(code);
        return ApiResponse.<AuthResponse>builder().data(data).message("Success").build();
    }

    @PostMapping("/verification")
    ApiResponse<Void> verify(@RequestBody @Valid VerifyRequest request) {
        verificationService.verifyCode(request.getEmail(), request.getCode(), request.getType());
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/resend-verification")
    ApiResponse<Void> resendVerification(@RequestBody @Valid ResendVerificationRequest request) {
        verificationService.resendVerificationCode(request.getEmail(), request.getType());
        return ApiResponse.<Void>builder().message("Success").build();
    }

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

    @PostMapping("/change-password")
    ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        authService.changePassword(request.getUsername(), request.getCurrentPassword(), request.getNewPassword(), request.getIsHavePassword());
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/reset-password")
    ApiResponse<Void> resetPassword(@RequestBody ResetPasswordRequest request) {
        authService.resetPassword(request.getEmail(), request.getNewPassword());
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PostMapping("/deactivate")
    ApiResponse<Void> deactivate(@RequestBody DeactivateRequest request) throws ParseException, JOSEException {
        authService.deactivate(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }
}
