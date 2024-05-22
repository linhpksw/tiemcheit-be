package com.tiemcheit.tiemcheitbe.controller;

import com.tiemcheit.tiemcheitbe.dto.CategoryDTO;
import com.tiemcheit.tiemcheitbe.dto.ProductDTO;
import com.tiemcheit.tiemcheitbe.service.CategoryService;
import com.tiemcheit.tiemcheitbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeScreenController {
    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductService productService;

    // this request is:  http://localhost:8080/api/home/categories
    @GetMapping("/categories")
    public List<CategoryDTO> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // this request is:  http://localhost:8080/api/home/category_id/{cid}
    @GetMapping("/category_id/{cid}")
    public List<ProductDTO> getAllProductsByCategoryID(@PathVariable Long cid) {
        return productService.getAllProductsByCategoryId(cid);
    }
}
