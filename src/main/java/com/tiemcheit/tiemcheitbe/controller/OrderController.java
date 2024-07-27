package com.tiemcheit.tiemcheitbe.controller;

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

    @GetMapping("/user/{uid}")
    public ApiResponse<List<OrderResponse>> getOrderByUser(@PathVariable Long uid) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getOrdersByUser(uid))
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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDate,
            @RequestParam(required = false) String status) {
        return ApiResponse.<List<OrderResponse>>builder()
                .data(orderService.getFilterOrdersByAdmin(startDate, endDate, status))
                .message("Success")
                .build();
    }

//    @PostMapping("/add")
//    public ApiResponse<Long> addOrder(@RequestBody OrderRequest request, @RequestParam(required = false) String code) {
//        return ApiResponse.<Long>builder()
//                .data(orderService.placeOrder(request, code))
//                .message("Success")
//                .build();
//    }

    @PatchMapping("/{orderId}/status")
    public ApiResponse<Void> updateOrderByUser(@PathVariable Long orderId,
                                               @RequestParam String status) {
        orderService.updateOrderStatus(orderId, status);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PatchMapping("/status")
    public ApiResponse<Void> updateOrderByUser(@RequestBody List<Long> orders, @RequestParam String status) {
        orderService.updateOrdersStatus(status, orders);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PatchMapping("/{orderId}/confirm")
    public ApiResponse<Void> updateOrderByUser(@PathVariable Long orderId) {
        orderService.updateOrderStatus(orderId, "Order Confirmed");
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @GetMapping("/status/{status}")
    public ApiResponse<Integer> getDeliveredOrders(@PathVariable String status) {
        return ApiResponse.<Integer>builder()
                .data(orderService.getOrdersAmountByStatus(status))
                .message("Success")
                .build();
    }

    @GetMapping("/count/{status}/{year}")
    public ApiResponse<Long[]> countDeliveredOrdersByMonth(@PathVariable String status, @PathVariable int year) {
        return ApiResponse.<Long[]>builder()
                .data(orderService.countDeliveredOrdersByMonth(status, year))
                .message("Success")
                .build();
    }

    @PatchMapping("/{orderId}/cancel-request")
    public ApiResponse<Void> cancelOrderRequest(@PathVariable Long orderId, @RequestParam String reason) {
        orderService.cancelOrderRequest(orderId, reason);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PatchMapping("/{orderId}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
        return ApiResponse.<Void>builder().message("Success").build();
    }

    @PatchMapping("/{orderId}/cancel-reject")
    public ApiResponse<Void> cancelOrderReject(@PathVariable Long orderId) {
        orderService.cancelOrderReject(orderId);
        return ApiResponse.<Void>builder().message("Success").build();
    }
}
