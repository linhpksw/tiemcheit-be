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
@RequestMapping("/orders")
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

    @GetMapping("/admin")
    public ApiResponse<List<OrderResponse>> getAllOrder() {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getAllOrders())
                .message("Success")
                .build();
    }


    @GetMapping("/filter")
    public ApiResponse<List<OrderResponse>> getOrdersByDateRangeAndStatus(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) String status) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getFilterOrders(startDate, endDate, status))
                .message("Success")
                .build();
    }

    @GetMapping("/admin/filter")
    public ApiResponse<List<OrderResponse>> getFilterOrdersByAdmin(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam String status) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getFilterOrdersByAdmin(startDate, endDate, status))
                .message("Success")
                .build();
    }

    @PostMapping("/add")
    public ApiResponse<Long> addOrder(@RequestBody OrderRequest request, @RequestParam String code) {
        return ApiResponse.<Long>builder()
                .data(orderService.placeOrder(request, code))
                .message("Success")
                .build();
    }

    @PatchMapping("/{orderId}/status")
    public ApiResponse<Void> updateOrderByUser(@PathVariable Long orderId,
                                               @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ApiResponse.<Void>builder().message("Success").build();
    }
}
