package com.tiemcheit.tiemcheitbe.dto.request;

import lombok.Data;

@Data
public class FeedbackRequest {
    private Long id;
    private String name;
    private String email;
    private String message;
    private boolean isRead;
}
