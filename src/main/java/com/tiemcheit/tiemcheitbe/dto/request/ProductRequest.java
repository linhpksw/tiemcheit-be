package com.tiemcheit.tiemcheitbe.dto.request;

import com.tiemcheit.tiemcheitbe.model.Category;
import com.tiemcheit.tiemcheitbe.model.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class ProductRequest {
    private Long id;
    private String name;
    private List<String> imageList;
    private Double price;
    private Category category;
    private LocalDate createAt;
    //    private Integer quantity;
    private String description;
    private List<Long> optionId;
    private List<ProductIngredientRequest> productIngredients;
    private String status;
    private String prevStatus;
    private User user;
}
