package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OptionResponse {
    private Long id;
    private String name;
    private Set<OptionValueResponse> optionValues;
}
