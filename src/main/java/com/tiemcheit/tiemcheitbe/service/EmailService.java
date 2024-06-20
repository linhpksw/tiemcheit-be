package com.tiemcheit.tiemcheitbe.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    public void sendVerificationCode(String to, String code) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@tiemcheit.com");
            helper.setTo(to);
            helper.setSubject("Mã xác minh Tiệm chè IT");

            String htmlMsg = "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +

                    "<style>" +
                    ".code {font-size: 24px; font-weight: bold; color: #007BFF;}" +
                    "</style>" +

                    "</head>" +
                    "<body>" +
                    "<h2>Xin chào,</h2>" +
                    "<p>Mã xác minh của bạn là: <span class='code'>" + code + "</span></p>" +
                    "<p>Mã này sẽ hết hạn sau 1 giờ. Vui lòng không chia sẻ mã này với bất kỳ ai khác.</p>" +

                    "</body>" +
                    "</html>";

            helper.setText(htmlMsg, true); // Set to true to send HTML

            emailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
