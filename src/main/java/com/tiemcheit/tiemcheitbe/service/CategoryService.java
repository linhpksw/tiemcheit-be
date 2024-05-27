package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.CategoryResponse;
import com.tiemcheit.tiemcheitbe.mapper.CategoryMapper;
import com.tiemcheit.tiemcheitbe.model.Category;
import com.tiemcheit.tiemcheitbe.repository.CategoryRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor

public class CategoryService {

    private final CategoryRepo categoryRepository;

//    public List<CategoryRequest> getAllCategories() {
//        List<Category> categories = categoryRepository.findAll();
//        return categories.stream()
//                .map(this::convertToDto)
//                .collect(Collectors.toList());
//    }

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper.INSTANCE::toCategoryResponse)
                .collect(Collectors.toList());
    }

}
