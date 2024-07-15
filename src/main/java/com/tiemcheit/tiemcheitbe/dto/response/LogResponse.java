package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LogResponse {
    private Long id;
    private Date timestamp;
    private String username;
    private String apiEndpoint;
    private String requestMethod;
    private Integer responseStatus;
    private String message;
    private Double executionTime;
    private String userAgent;
}
