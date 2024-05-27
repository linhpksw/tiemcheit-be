package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

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




    public List<ProductResponse> getProductByConditionsAndSort(Map<String, String> conditions,
                                                               String sortField,
                                                               String sortDirection) 
    {
        Specification<Product> specification = ProductSpecification.getSpecification(conditions);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);

        return productRepo.findAll(specification, sort)
                .stream()
                .map(ProductMapper.INSTANCE::toProductResponse)
                .collect(Collectors.toList());

    }

}
