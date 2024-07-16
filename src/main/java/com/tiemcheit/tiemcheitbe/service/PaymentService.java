package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.OrderRequest;
import com.tiemcheit.tiemcheitbe.dto.request.PaymentRequest;
import com.tiemcheit.tiemcheitbe.dto.response.PaymentResponse;
import com.tiemcheit.tiemcheitbe.mapper.PaymentMapper;
import com.tiemcheit.tiemcheitbe.model.CassoTransaction;
import com.tiemcheit.tiemcheitbe.model.Payment;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.repository.PaymentRepo;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final UserRepo userRepo;
    private final PaymentMapper paymentMapper;
    private final OrderService orderService;

    public void addPayment(PaymentRequest request) {
        User user = userRepo.findByUsername(request.getUsername()).orElseThrow(() -> new AppException("User not found.", HttpStatus.NOT_FOUND));

        if (user != null) {
            paymentRepo.save(
                    Payment.builder()
                            .orderDate(request.getOrderDate())
                            .username(request.getUsername())
                            .shippingAddress(request.getShippingAddress())
                            .shippingMethod(request.getShippingMethod())
                            .paymentMethod(request.getPaymentMethod())
                            .discountPrice(request.getDiscountPrice())
                            .message(request.getMessage())
                            .totalPrice(request.getTotalPrice())
                            .build());
        }
    }

    public PaymentResponse getPayment(String username) {
        Payment payment = paymentRepo.findTopByUsernameOrderByOrderDateDesc(username)
                .orElseThrow(() -> new AppException("No payments found for user.", HttpStatus.NOT_FOUND));

        return paymentMapper.toPaymentResponse(payment);
    }

    public void handleWebhook(CassoTransaction transaction) {
        String username = transaction.getDescription();
        Long amount = transaction.getAmount();

        Payment verifiedPayment = verifyPayment(username, amount);

        if (verifiedPayment != null) {

//            log.info("handle success");
            OrderRequest orderRequest = OrderRequest.builder()
                    .orderDate(new Date())
                    .shippingAddress(verifiedPayment.getShippingAddress())
                    .shippingMethod(verifiedPayment.getShippingMethod())
                    .paymentMethod(verifiedPayment.getPaymentMethod())
                    .discountPrice(verifiedPayment.getDiscountPrice())
                    .message(verifiedPayment.getMessage())
                    .build();

            orderService.placeOrder(orderRequest, null, username);
        }
    }

    private Payment verifyPayment(String username, Long amount) {
        return paymentRepo.findTopByUsernameAndTotalPriceOrderByOrderDateDesc(username, amount)
                .orElse(null);
    }

}
