package com.tiemcheit.tiemcheitbe.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "logs")
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String apiEndpoint;

    @Column(nullable = false)
    private String requestMethod;

    @Column
    private Integer responseStatus;

    @Column(length = 512)
    private String message;

    @Column
    private Double executionTime; // Store execution time in milliseconds

    @Column(length = 512)
    private String userAgent;
    
    @Column(updatable = false)
    private Date timestamp;

}
