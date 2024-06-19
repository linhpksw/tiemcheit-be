package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.model.User;
import com.tiemcheit.tiemcheitbe.model.VerificationCode;
import com.tiemcheit.tiemcheitbe.repository.UserRepo;
import com.tiemcheit.tiemcheitbe.repository.VerificationCodeRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationService {
    private final VerificationCodeRepo verificationCodeRepo;
    private final EmailService emailService;
    private final UserRepo userRepo;

    public VerificationCode generateVerificationCode(User user) {
        Random random = new Random();
        int num = 100000 + random.nextInt(900000); // 6-digit code
        int expiryTime = 1000 * 60 * 60; // 1 hour

        log.info("Generated verification code: {}", num);

        return VerificationCode.builder()
                .code(String.valueOf(num))
                .user(user)
                .expiresAt(new Date(System.currentTimeMillis() + expiryTime))
                .build();
    }

    public void verifyCode(String email, String code, String type) throws AppException {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        log.info("User: {}", user);

        boolean codeFound = user.getVerificationCodes().stream()
                .anyMatch(vc -> vc.getCode().equals(code) && !vc.isExpired());

        if (!codeFound) {
            throw new AppException("Verification code not found or expired", HttpStatus.BAD_REQUEST);
        }

        if (type.equals("verify")) {
            user.setIsActivated(true);
        }

        verificationCodeRepo.deleteByUserId(user.getId());
        user.getVerificationCodes().clear();
        userRepo.save(user);
    }

    public void sendVerificationCode(String email, String code) {
        emailService.sendVerificationCode(email, code);
    }

    public void resendVerificationCode(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));

        if (user.getIsActivated()) {
            throw new AppException("User is already activated", HttpStatus.BAD_REQUEST);
        }

        verificationCodeRepo.deleteByUserId(user.getId());
        user.getVerificationCodes().clear();

        VerificationCode newCode = generateVerificationCode(user);

        List<VerificationCode> verificationCodes = new ArrayList<>();
        verificationCodes.add(newCode);
        user.setVerificationCodes(verificationCodes);

        userRepo.save(user);

        emailService.sendVerificationCode(user.getEmail(), newCode.getCode());
    }
}
