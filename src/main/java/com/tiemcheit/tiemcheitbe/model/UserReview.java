package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "user_review")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "ordered_product_id", nullable = false)
    private OrderDetail orderDetail;
    private int ratingValue;
    private String comment;
    @Builder.Default
    private LocalDateTime createTime = LocalDateTime.now();
    @Builder.Default
    private LocalDateTime updatedTime = LocalDateTime.now();
}
