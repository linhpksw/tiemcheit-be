package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.model.CassoTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CassoRequest {
    private int error;
    private List<CassoTransaction> data;
}
