package com.tiemcheit.tiemcheitbe.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ApiResponse<?> apiResponse = ApiResponse.builder()
                .error(HttpServletResponse.SC_UNAUTHORIZED)
                .message("Unauthorized")
                .build();

        ObjectMapper objectMapper = new ObjectMapper();

        response.getWriter().write(objectMapper.writeValueAsString(apiResponse));
        response.flushBuffer();
    }
}
