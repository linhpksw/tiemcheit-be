package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.OrderRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.OrderMapper;
import com.tiemcheit.tiemcheitbe.model.Order;
import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.OrderRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final OrderMapper orderMapper;
    private final CartService cartService;

    public List<OrderResponse> getUserOrders() {
        User user = userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        return orderMapper.toResponses(orderRepo.findAllByUserOrderByIdDesc(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getAllOrders() {
        return orderMapper.toResponses(orderRepo.findAllByUserOrderDateDesc());
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<OrderResponse> getOrdersByUser(Long uid) {
        User user = userRepo.getReferenceById(uid);
        return orderMapper.toResponses(orderRepo.findAllByUser(user));
    }

    // check the not found exception after
    public OrderResponse getOrderDetails(Long orderId) {
        User user = userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));

        if (userHasRole(user, "ADMIN") || order.getUser().getId().equals(user.getId())) {
            return orderMapper.toReponse(order);
        } else {
            // test
            throw new AppException("Access denied", HttpStatus.FORBIDDEN);
        }

    }

    public List<OrderResponse> getFilterOrders(Date startDate, Date endDate, String status) {
        User user = userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        return orderMapper.toResponses(orderRepo.findAllByUserIdAndOptionalFilters(user.getId(), startDate, endDate, status));
    }

    public List<OrderResponse> getFilterOrdersByAdmin(Date startDate, Date endDate, String status) {
        return orderMapper.toResponses(orderRepo.findAllByOptionalFilters(startDate, endDate, status));
    }

    public Long placeOrder(OrderRequest request) {
        // first get the item from user's cart
        List<CartItemResponse> cartItemList = cartService.allCartItems();

        // Create a new order
        Order order = new Order();
        order.setOrderDate(new Date());

        // Set other order properties like shipping address, shipping method, payment method, order status
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingMethod(request.getShippingMethod()); // Replace with actual data
        order.setPaymentMethod(request.getPaymentMethod()); // Replace with actual data
        order.setMessage(request.getMessage());
        order.setOrderStatus("Order Received"); // Replace with actual data

        // Retrieve the user

        User user = userRepo.findByUsername(SecurityUtils.getCurrentUsername()).orElseThrow(() -> new RuntimeException("User not found"));
        order.setUser(user);

        // Add order items
        List<OrderDetail> orderDetails = cartItemList.stream().map(cartItem -> {
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProduct().getPrice());

            // Retrieve the product and set it to the order detail
            Product product = productRepo.findById(cartItem.getProduct().getId())
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            orderDetail.setProduct(product);

            orderDetail.setOrder(order);
            return orderDetail;
        }).collect(Collectors.toList());

        order.setOrderDetails(orderDetails);

        // Clear the user's cart
        cartService.clearCart();

        // Save the order and order details
        return orderRepo.save(order).getId();
    }

    @Transactional
    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Update the status
        order.setOrderStatus(status);

        // Save the updated order
        orderRepo.save(order);
    }

    private boolean userHasRole(User user, String role) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals(role));
    }
}
