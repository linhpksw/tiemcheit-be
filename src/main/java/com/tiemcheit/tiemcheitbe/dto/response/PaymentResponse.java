package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private Date orderDate;
    private String shippingAddress;
    private String shippingMethod;
    private String paymentMethod;
    private String message;
    private Double discountPrice;
    private Double totalPrice;
    private String username;
}
