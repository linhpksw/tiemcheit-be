package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.OrderRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
import com.tiemcheit.tiemcheitbe.mapper.OrderMapper;
import com.tiemcheit.tiemcheitbe.model.Order;
import com.tiemcheit.tiemcheitbe.model.OrderDetail;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.OrderRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public List<OrderResponse> listOrders(User user) {
        return orderMapper.toResponses(orderRepo.findAllByUserOrderByIdDesc(user));
    }

    // check the not found exception after
    public OrderResponse getOrder(Long id) {
        return orderMapper.toReponse(orderRepo.findById(id).orElseThrow(() -> new RuntimeException("Order not found")));
    }

    public void placeOrder(Long uid, OrderRequest request) {
        // first get the item from user's cart
        List<CartItemResponse> cartItemList = cartService.allCartItems(uid);

        // Create a new order
        Order order = new Order();
        order.setOrderDate(new Date());

        // Set other order properties like shipping address, shipping method, payment method, order status
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingMethod(request.getShippingMethod()); // Replace with actual data
        order.setPaymentMethod(request.getPaymentMethod()); // Replace with actual data
        order.setOrderStatus("Pending"); // Replace with actual data

        // Retrieve the user

        User user = userRepo.findById(1L).orElseThrow(() -> new RuntimeException("User not found"));
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

        // Save the order and order details
        orderRepo.save(order);

        // Clear the user's cart
        cartService.clearCart(uid);
    }
}
