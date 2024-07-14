package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Data;

@Data
public class FeedbackResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String message;
    private boolean isRead;
}
