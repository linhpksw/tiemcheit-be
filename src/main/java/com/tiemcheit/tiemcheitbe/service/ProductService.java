package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepo productRepository;

    //get All ProductDTO by category id
    public List<ProductResponse> getAllProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return products.stream()
                .map(ProductMapper.INSTANCE::toProductResponse)
                .collect(Collectors.toList());
    }
}
