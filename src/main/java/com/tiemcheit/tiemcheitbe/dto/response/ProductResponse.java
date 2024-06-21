package com.tiemcheit.tiemcheitbe.dto.response;

import com.tiemcheit.tiemcheitbe.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Double price;
    private String image;
    private LocalDate createAt;
    private Integer quantity;
    private Category category;
    private String status;
}