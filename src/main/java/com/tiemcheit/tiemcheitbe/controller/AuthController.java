package com.tiemcheit.tiemcheitbe.controller;

import com.nimbusds.jose.JOSEException;
import com.tiemcheit.tiemcheitbe.dto.request.AuthRequest;
import com.tiemcheit.tiemcheitbe.dto.request.IntrospectRequest;
import com.tiemcheit.tiemcheitbe.dto.request.LogoutRequest;
import com.tiemcheit.tiemcheitbe.dto.request.RefreshRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.AuthResponse;
import com.tiemcheit.tiemcheitbe.dto.response.IntrospectResponse;
import com.tiemcheit.tiemcheitbe.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @GetMapping("/hello-world")
    public String welcome() {
        return "Welcome to Tiem Che IT!";
    }

    @PostMapping("/login")
    ApiResponse<AuthResponse> authenticate(@RequestBody AuthRequest request) {
        var data = authService.authenticate(request);
        return ApiResponse.<AuthResponse>builder()
                .message("Success")
                .data(data).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> authenticate(@RequestBody IntrospectRequest request)
            throws ParseException, JOSEException {
        var data = authService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(data)
                .build();
    }

    @PostMapping("/refresh")
    ApiResponse<AuthResponse> authenticate(@RequestBody RefreshRequest request)
            throws ParseException, JOSEException {
        var data = authService.refreshToken(request);
        return ApiResponse.<AuthResponse>builder().data(data).build();
    }

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        authService.logout(request);
        return ApiResponse.<Void>builder().build();
    }


}
