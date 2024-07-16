package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.model.Coupon;
import com.tiemcheit.tiemcheitbe.model.User;
import lombok.Data;

@Data
public class SendCouponRequest {
    private User user;
    private Coupon coupon;
}
