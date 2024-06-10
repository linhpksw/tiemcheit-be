package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.CategoryResponse;
import com.tiemcheit.tiemcheitbe.mapper.CategoryMapper;
import com.tiemcheit.tiemcheitbe.model.Category;
import com.tiemcheit.tiemcheitbe.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepository;

    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(CategoryMapper.INSTANCE::toCategoryResponse)
                .toList();
    }

    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepository.getReferenceById(id);
        return CategoryMapper.INSTANCE.toCategoryResponse(category);
    }

}
