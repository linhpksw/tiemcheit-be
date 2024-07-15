package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.model.Coupon;
import com.tiemcheit.tiemcheitbe.model.User;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;

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

    public void sendCouponCode(User user, Coupon coupon) {
        try {
            MimeMessage message = emailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom("noreply@tiemcheit.com");
            helper.setTo(user.getEmail());
            helper.setSubject("Mã giảm giá Tiệm chè IT");

            String htmlMsg = "<!DOCTYPE html>\n" +
                    "<html>\n" +
                    "<head>\n" +
                    "    <title>Mã giảm giá của bạn</title>\n" +
                    "    <style>\n" +
                    "        body {\n" +
                    "            font-family: Arial, sans-serif;\n" +
                    "        }\n" +
                    "        .container {\n" +
                    "            margin: 0 auto;\n" +
                    "            padding: 20px;\n" +
                    "            max-width: 600px;\n" +
                    "            background-color: #f9f9f9;\n" +
                    "            border: 1px solid #e0e0e0;\n" +
                    "            border-radius: 10px;\n" +
                    "        }\n" +
                    "        h1 {\n" +
                    "            color: #333;\n" +
                    "        }\n" +
                    "        .coupon-code {\n" +
                    "            font-size: 1.5em;\n" +
                    "            font-weight: bold;\n" +
                    "            color: #d9534f;\n" +
                    "            background-color: #f2dede;\n" +
                    "            padding: 10px;\n" +
                    "            border: 1px solid #ebccd1;\n" +
                    "            border-radius: 5px;\n" +
                    "        }\n" +
                    "        .details {\n" +
                    "            margin-top: 20px;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "    <div class=\"container\">\n" +
                    "        <h1>Chúc mừng!</h1>\n" +
                    "        <p>Xin chào " + user.getUsername() + ",</p>\n" +
                    "        <p>Chúng tôi rất vui khi được chia sẻ mã giảm giá cho bạn với sự đóng góp lớn của bạn cho cửa hàng của chúng tôi:</p>\n" +
                    "        <div class=\"coupon-code\">" + coupon.getCode() + "</div>\n" +
                    "        <p class=\"details\">\n" +
                    "            <strong>Mô tả:</strong>" + coupon.getDescription() + "<br>\n" +
                    "            <strong>Ngày hợp lệ: </strong>" + new SimpleDateFormat("HH:mm dd:MM:yyyy").format(coupon.getDateValid()) + "<br>\n" +
                    "            <strong>Ngày hết hạn: </strong>" + new SimpleDateFormat("HH:mm dd:MM:yyyy").format(coupon.getDateValid()) + "<br>\n" +
                    "            <strong>Giới hạn sử dụng: </strong>" + coupon.getLimitAccountUses() + "<br>\n" +
                    "        </p>\n" +
                    "        <p>Hãy chắc chắn để sử dụng nó trước khi nó hết hạn!</p>\n" +
                    "        <p>Trân trọng,<br>Tiệm chè IT</p>\n" +
                    "    </div>\n" +
                    "</body>\n" +
                    "</html>";

            helper.setText(htmlMsg, true); // Set to true to send HTML

            emailSender.send(message);
        } catch (jakarta.mail.MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}
