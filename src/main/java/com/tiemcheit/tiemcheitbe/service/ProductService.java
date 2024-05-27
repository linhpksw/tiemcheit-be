package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import com.tiemcheit.tiemcheitbe.service.specification.ProductSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProductService {
    @Autowired
    private ProductRepo productRepo;

    public List<ProductResponse> getProductByConditionsAndSort(Map<String, String> conditions,
                                                               String sortField,
                                                               String sortDirection) {
        Specification<Product> specification = ProductSpecification.getSpecification(conditions);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);

        return productRepo.findAll(specification, sort)
                .stream()
                .map(ProductMapper.INSTANCE::toProductResponse)
                .collect(Collectors.toList());

    }

}
