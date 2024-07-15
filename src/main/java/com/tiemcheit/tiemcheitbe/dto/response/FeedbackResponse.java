package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

import java.util.Date;

@Data
public class FeedbackResponse {
    private Long id;
    private String name;
    private String email;
    private String message;
    private Date sentAt;
    private boolean isRead;
}
