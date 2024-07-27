package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.OrderRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CartItemResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OrderResponse;
import com.tiemcheit.tiemcheitbe.mapper.OrderMapper;
import com.tiemcheit.tiemcheitbe.model.*;
import com.tiemcheit.tiemcheitbe.repository.*;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepo orderRepo;
    private final ProductRepo productRepo;
    private final ProductIngredientRepo productIngredientRepo;
    private final UserRepo userRepo;
    private final ProductImageRepo productImageRepo;
    private final OrderMapper orderMapper;
    private final CartService cartService;
    private final CouponService couponService;
    private final IngredientRepo ingredientRepo;

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
        Order order = orderRepo.findById(orderId).orElseThrow(() -> new AppException("Order not found", HttpStatus.NOT_FOUND));

        if (userHasRole(user, "ADMIN") || order.getUser().getId().equals(user.getId())) {
            var orderResponse = orderMapper.toReponse(order);

            orderResponse.getOrderDetails().forEach(orderDetailResponse -> {
                orderDetailResponse.getProduct().setImage(productImageRepo.findAllByProductId(orderDetailResponse.getProduct().getId()).stream()
                        .findFirst()
                        .map(ProductImage::getImage)
                        .orElse(null));

            });

            return orderResponse;
        } else {

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

    @Transactional
    public void placeOrder(OrderRequest request, String code, String username) {
        List<CartItemResponse> cartItemList = cartService.allCartItemsFromUsername(username);

        Order order = new Order();
        order.setOrderDate(new Date());

        // Set other order properties like shipping address, shipping method, payment method, order status
        order.setShippingAddress(request.getShippingAddress());
        order.setShippingMethod(request.getShippingMethod()); // Replace with actual data
        order.setPaymentMethod(request.getPaymentMethod()); // Replace with actual data
        order.setMessage(request.getMessage());
        order.setOrderStatus("Order Received"); // Replace with actual data

        // set coupon to order if having code
        if (code != null) {
            Coupon coupon = couponService.getCouponByCode(code);
            order.setCoupon(coupon);
            if (coupon != null)
                coupon.setUseCount(coupon.getUseCount() + 1);
        }
        order.setDiscountPrice(request.getDiscountPrice());

        // Retrieve the user
        User user = userRepo.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
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

            // Reduce quantities of ingredients for the product
            List<ProductIngredient> productIngredients = productIngredientRepo.findAllByProductIdWithLock(cartItem.getProduct().getId());
            for (ProductIngredient productIngredient : productIngredients) {
                Ingredient ingredient = productIngredient.getIngredient();
                float unitsNeeded = productIngredient.getUnit() * cartItem.getQuantity();

                if (ingredient.getQuantity() < unitsNeeded) {
                    throw new AppException("Không đủ nguyên liệu cho " + productIngredient.getProduct().getName(), HttpStatus.BAD_REQUEST);
                }
                ingredient.setQuantity(ingredient.getQuantity() - (long) unitsNeeded);
                ingredientRepo.save(ingredient);
            }


            orderDetail.setOrder(order);
            return orderDetail;
        }).collect(Collectors.toList());

        order.setOrderDetails(orderDetails);

        // Clear the user's cart
        cartService.clearCart(username);

        // Save the order and order details
        orderRepo.save(order);
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

    @Transactional
    public int updateOrdersStatus(String status, List<Long> orders) {
        return orderRepo.updateOrderStatusByIds(status, orders);
    }

    @Transactional
    public void cancelOrderRequest(Long orderId, String reason) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Update the status
        order.setCancelReason(reason);
        order.setOrderStatus("Cancel Pending");
        // Save the updated order
        orderRepo.save(order);
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setOrderStatus("Order Canceled");
        // Save the updated order
        orderRepo.save(order);

        // restock ingredient quantity
        restockWhenCancel(orderId);
    }

    @Transactional
    public void cancelOrderReject(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        order.setOrderStatus("Order Received");
        // Save the updated order
        orderRepo.save(order);
    }

    private boolean userHasRole(User user, String role) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals(role));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Integer getSuccessOrdersAmount() {
        String status = "DELIVERED";
        List<Order> orderList = orderRepo.findAllByOrderStatus(status);
        return orderList.size();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Integer getOrdersAmountByStatus(String status) {
        List<Order> orderList = orderRepo.findAllByOrderStatus(status);
        return orderList.size();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Long[] countDeliveredOrdersByMonth(String status, int year) {
        List<Object[]> results = orderRepo.countOrdersByStatusAndMonth(status.toUpperCase(), year);
        Long[] countByMonth = new Long[12];
        Arrays.fill(countByMonth, 0L);
        for (Object[] result : results) {
            Integer month = (Integer) result[0] - 1; // Month is 1-based in SQL, adjust to 0-based for array
            Long count = (Long) result[1];
            countByMonth[month] = count;
        }
        return countByMonth;
    }

    @Transactional
    protected void restockWhenCancel(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + orderId));

        // Restock quantities of ingredients for the product
        for (OrderDetail response : order.getOrderDetails()) {
            List<ProductIngredient> productIngredients = productIngredientRepo.findAllByProductIdWithLock(response.getProduct().getId());
            for (ProductIngredient productIngredient : productIngredients) {
                Ingredient ingredient = productIngredient.getIngredient();
                float units = productIngredient.getUnit() * response.getQuantity();

                ingredient.setQuantity(ingredient.getQuantity() + (long) units);
                ingredientRepo.save(ingredient);
            }
        }
    }

}
