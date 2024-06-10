package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserReviewResponse {
    private long id;
    private UserResponse user;
    private int ratingValue;
    private String comment;
    private LocalDateTime createTime;
    private LocalDateTime updatedTime;
}
