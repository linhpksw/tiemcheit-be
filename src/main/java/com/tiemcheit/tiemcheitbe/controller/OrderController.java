package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.OrderRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
import com.tiemcheit.tiemcheitbe.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {
    private final OrderService orderService;

    @GetMapping("")
    public ApiResponse<List<OrderResponse>> getUserOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getUserOrders())
                .message("Success")
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<OrderResponse> getOrderDetails(@PathVariable Long id) {
        return ApiResponse.<OrderResponse>builder()
                .data(orderService.getOrderDetails(id))
                .message("Success")
                .build();
    }

    @GetMapping("/admin/all")
    public ApiResponse<List<OrderResponse>> getAllOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getAllOrders())
                .message("Success")
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<Void> addOrder(@RequestBody OrderRequest request) {
        // just for test
        orderService.placeOrder(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

}
