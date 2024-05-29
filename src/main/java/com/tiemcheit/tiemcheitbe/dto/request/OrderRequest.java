package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.dto.response.OrderDetailResponse;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderRequest {
    private Date orderDate;
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String orderStatus;
    private List<OrderDetailResponse> orderDetails;
}
