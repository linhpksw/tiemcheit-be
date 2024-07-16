package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.PaymentRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.PaymentResponse;
import com.tiemcheit.tiemcheitbe.model.CassoTransaction;
import com.tiemcheit.tiemcheitbe.service.OrderService;
import com.tiemcheit.tiemcheitbe.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;
    private final OrderService orderService;


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
    public ApiResponse<Void> handleWebhook(@RequestBody CassoTransaction transaction) {
        paymentService.handleWebhook(transaction);
        
        return ApiResponse.<Void>builder().message("Success").build();
    }

}
