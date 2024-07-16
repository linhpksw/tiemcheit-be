package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.CategoryRequest;
import com.tiemcheit.tiemcheitbe.dto.response.CategoryResponse;
import com.tiemcheit.tiemcheitbe.mapper.CategoryMapper;
import com.tiemcheit.tiemcheitbe.model.Category;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.CategoryRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import com.tiemcheit.tiemcheitbe.repository.exception.AppException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    public List<CategoryResponse> getAllCategoriesByActiveStatus() {
        return categoryRepo.findAllByActiveStatus()
                .stream()
                .map(CategoryMapper.INSTANCE::toCategoryResponse)
                .toList();
    }


    public CategoryResponse getCategoryById(Long id) {
        Category category = categoryRepo.getReferenceById(id);
        return CategoryMapper.INSTANCE.toCategoryResponse(category);
    }

    //=============================================FOR ADMINS=======================================================
    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categories.stream()
                .map(CategoryMapper.INSTANCE::toCategoryResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse create(CategoryRequest request) {
        List<Category> foundCategory = categoryRepo.findByName(request.getName());
        for (Category category : foundCategory) {
            if (category.getName().equals(request.getName())) {
                throw new AppException("Category already exists", HttpStatus.BAD_REQUEST);
            }
        }
        Category category = CategoryMapper.INSTANCE.toCategory(request);
        categoryRepo.save(category);
        return CategoryMapper.INSTANCE.toCategoryResponse(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<CategoryResponse> getAllCategoriesByStatus(String status) {
        return categoryRepo.findAllByStatus(status)
                .stream()
                .map(CategoryMapper.INSTANCE::toCategoryResponse)
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public CategoryResponse update(Long id, CategoryRequest request) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));
        if (!request.getName().isEmpty()) {
            category.setName(request.getName());
        }
        if (!request.getStatus().isEmpty()) {
            category.setStatus(request.getStatus());
        }
        Category updatedCategory = categoryRepo.save(category);
        return CategoryMapper.INSTANCE.toCategoryResponse(updatedCategory);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean delete(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));
        categoryRepo.delete(category);
        return true;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public CategoryResponse updateCategoryStatus(Long categoryId, String newStatus, String flag) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        category.setStatus(newStatus);
        categoryRepo.save(category);

        // Cập nhật status của các sản phẩm trong danh mục
        if ("disabled".equals(newStatus)) {
            this.updateProductStatusByCategory(categoryId, "disabled");
        } else if ("active".equals(newStatus)) {
            if ("restore".equals(flag)) {
                this.restoreProductStatusByCategory(categoryId);
            } else if ("all".equals(flag)) {
                this.updateProductStatusByCategory(categoryId, "active");
            }
        }
        return CategoryMapper.INSTANCE.toCategoryResponse(category);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void updateProductStatusByCategory(Long categoryId, String newStatus) {
        Category category = categoryRepo.findById(categoryId)
                .orElseThrow(() -> new AppException("Category not found", HttpStatus.NOT_FOUND));

        List<Product> products = productRepo.findAllByCategoryId(category.getId());
        for (Product product : products) {
            if (product.getStatus().equals("inactive")) {
                continue;
            }
            product.setPrevStatus(product.getStatus());
            product.setStatus(newStatus);
            productRepo.save(product);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void restoreProductStatusByCategory(Long categoryId) {
        List<Product> products = productRepo.findAllByCategoryId(categoryId);
        for (Product product : products) {
            if (product.getStatus().equals("inactive")) {
                continue;
            }
            product.setStatus(product.getPrevStatus());
            productRepo.save(product);
        }
    }
}
