package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private UserInfoResponse user;
    private Date orderDate;
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String orderStatus;
    private List<OrderDetailResponse> orderDetails;
}
