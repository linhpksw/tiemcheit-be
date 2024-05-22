package com.tiemcheit.tiemcheitbe.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class OrderDto {
    private Long id;
    private Date orderDate;
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String orderStatus;
    private List<OrderDetailDto> orderDetails;
}
