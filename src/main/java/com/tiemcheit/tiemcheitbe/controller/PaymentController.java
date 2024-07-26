package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.CassoRequest;
import com.tiemcheit.tiemcheitbe.dto.request.PaymentRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.PaymentResponse;
import com.tiemcheit.tiemcheitbe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping()
    public ApiResponse<Void> addPayment(@RequestBody PaymentRequest request) {
        paymentService.addPayment(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @GetMapping("/{username}")
    public ApiResponse<PaymentResponse> getPayment(@PathVariable String username) {
        return ApiResponse.<PaymentResponse>builder().data(paymentService.getPayment(username)).message("Success").build();
    }

    @PostMapping("/casso")
    public ApiResponse<Void> handleWebhook(@RequestBody CassoRequest request) {
        if (request == null || request.getData() == null || request.getData().isEmpty()) {
            log.error("Received null or empty transaction list");
            return ApiResponse.<Void>builder().message("Transaction list is null or empty").build();
        }

        request.getData().forEach(paymentService::handleWebhook);

        return ApiResponse.<Void>builder().message("Success").build();
    }

    @GetMapping("/check/{username}")
    public ApiResponse<Boolean> isPaid(@PathVariable String username) {
        boolean exists = paymentService.checkPaymentExists(username);
        return ApiResponse.<Boolean>builder().data(!exists).message("Success").build();
    }

}
