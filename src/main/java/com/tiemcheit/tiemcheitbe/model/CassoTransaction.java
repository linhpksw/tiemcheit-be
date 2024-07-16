package com.tiemcheit.tiemcheitbe.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CassoTransaction {
    private Long id;
    private String tid;
    private String description;
    private Long amount;
}
