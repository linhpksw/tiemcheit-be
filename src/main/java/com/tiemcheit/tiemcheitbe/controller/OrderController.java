package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.request.OrderRequest;
import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
import com.tiemcheit.tiemcheitbe.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
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

    @GetMapping("/date-range")
    public ApiResponse<List<OrderResponse>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getOrdersByDateRange(startDate, endDate))
                .message("Success")
                .build();
    }

    @GetMapping("/status")
    public ApiResponse<List<OrderResponse>> getOrdersByStatus(@RequestParam String status) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getOrdersByStatus(status))
                .message("Success")
                .build();
    }

    @GetMapping("/filter")
    public ApiResponse<List<OrderResponse>> getOrdersByDateRangeAndStatus(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam String status) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getOrdersByDateRangeAndStatus(startDate, endDate, status))
                .message("Success")
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<Void> addOrder(@RequestBody OrderRequest request) {
        orderService.placeOrder(request);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PatchMapping("/{orderId}/status")
    public ApiResponse<Void> addOrder(@PathVariable Long orderId,
                                      @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ApiResponse.<Void>builder().message("Success").build();
    }
}
