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
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentRepo paymentRepo;
    private final UserRepo userRepo;
    private final PaymentMapper paymentMapper;
    private final OrderService orderService;

    @Transactional
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
                            .couponCode(request.getCouponCode())
                            .message(request.getMessage())
                            .totalPrice(request.getTotalPrice())
                            .build());

        }
    }

    public PaymentResponse getPayment(String username) {
        Payment payment = paymentRepo.findTop1ByUsernameOrderByOrderDateDesc(username)
                .orElseThrow(() -> new AppException("No payments found for user.", HttpStatus.NOT_FOUND));

        return paymentMapper.toPaymentResponse(payment);
    }

    @Transactional
    public void handleWebhook(CassoTransaction transaction) {
        log.info("transaction {}", transaction);

        if (transaction == null) {
            log.error("Transaction is null");
            return;
        }

        String description = transaction.getDescription().toLowerCase();

        if (description == null) {
            log.error("Transaction description is null");
            return;
        }

        log.info("Transaction description: {}", description);
        Pattern pattern = Pattern.compile("den:\\S+ (\\w+)");
        Matcher matcher = pattern.matcher(description);

        if (matcher.find()) {
            String username = matcher.group(1);
            log.info("Extracted username: {}", username);

            if (username == null || username.isEmpty()) {
                log.error("Extracted username is null or empty");
                return;
            }

            Long amount = transaction.getAmount();
            Payment verifiedPayment = verifyPayment(username, amount);

            if (verifiedPayment == null) {
                log.error("No matching payment found for username: {} and amount: {}", username, amount);
                return;
            }
            OrderRequest orderRequest = OrderRequest.builder()
                    .orderDate(new Date())
                    .shippingAddress(verifiedPayment.getShippingAddress())
                    .shippingMethod(verifiedPayment.getShippingMethod())
                    .paymentMethod(verifiedPayment.getPaymentMethod())
                    .discountPrice(verifiedPayment.getDiscountPrice())
                    .message(verifiedPayment.getMessage())
                    .build();

            orderService.placeOrder(orderRequest, verifiedPayment.getCouponCode(), username);

            // Delete all payments for the username after placing the order
            paymentRepo.deleteByUsername(username);

        } else {
            log.error("Username not found in description: {}", description);
        }
    }

    private Payment verifyPayment(String username, Long amount) {
        return paymentRepo.findMatchingPayment(username, amount).orElse(null);
    }

    public boolean checkPaymentExists(String username) {
        Optional<Payment> payment = paymentRepo.findTop1ByUsernameOrderByOrderDateDesc(username);
        return payment.isPresent();
    }

}
