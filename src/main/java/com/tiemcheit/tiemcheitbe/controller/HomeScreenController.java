package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.CategoryDTO;
import com.tiemcheit.tiemcheitbe.dto.ProductDTO;
import com.tiemcheit.tiemcheitbe.service.CategoryService;
import com.tiemcheit.tiemcheitbe.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
@AllArgsConstructor
public class HomeScreenController {

    private final CategoryService categoryService;
    private final ProductService productService;

    // this request is:  http://localhost:8080/api/home/category
    @GetMapping("/category")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // this request is:  http://localhost:8080/api/home/allProductByCategory/{id}
    @GetMapping("/allProductByCategory/{id}")
    public List<ProductDTO> getAllProductsByCategoryID(@PathVariable Long id) {
        return productService.getAllProductsByCategoryId(id);
    }
}
