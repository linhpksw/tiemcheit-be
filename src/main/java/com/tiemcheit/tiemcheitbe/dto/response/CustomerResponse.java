package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
    private Long id;
    private String username;
    private String fullname;
    private String phone;
    private String email;
    private Date dob;
    private String status;
    private Date createdAt;

}