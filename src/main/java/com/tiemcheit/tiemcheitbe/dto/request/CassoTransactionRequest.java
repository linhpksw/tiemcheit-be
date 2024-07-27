package com.tiemcheit.tiemcheitbe.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.tiemcheit.tiemcheitbe.util.CustomDateDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CassoTransactionRequest {
    @JsonDeserialize(using = CustomDateDeserializer.class)
    private Date when;
    private String description;
    private Double amount;
}
