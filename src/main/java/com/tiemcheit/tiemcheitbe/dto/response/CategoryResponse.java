package com.tiemcheit.tiemcheitbe.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryResponse {
    private Long id;
    private String code;
    private String name;

}
