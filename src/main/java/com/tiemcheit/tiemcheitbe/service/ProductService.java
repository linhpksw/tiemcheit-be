package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.IngredientMapper;
import com.tiemcheit.tiemcheitbe.mapper.OptionMapper;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.model.ProductIngredient;
import com.tiemcheit.tiemcheitbe.model.ProductOption;
import com.tiemcheit.tiemcheitbe.repository.ProductIngredientRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductOptionRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import com.tiemcheit.tiemcheitbe.service.specification.ProductSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;
    private final ProductOptionRepo productOptionRepo;
    private final ProductIngredientRepo productIngredientRepo;

    private final OptionMapper optionMapper;

    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(ProductMapper.INSTANCE::toProductResponse)
                .collect(Collectors.toList());
    }

    //get All ProductResponse by category id
    public List<ProductResponse> getAllProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepo.findAllByCategoryId(categoryId);
        return products.stream()
                .map(ProductMapper.INSTANCE::toProductResponse)
                .collect(Collectors.toList());
    }


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

    //get ProductDetailResponse by product id
    public ProductDetailResponse getProductDetailById(Long productId) {
        Product product = productRepo.findById(productId).orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));

        List<OptionResponse> optionList = productOptionRepo.findAllByProductId(product.getId())
                .stream()
                .map(ProductOption::getOption)
                .map(optionMapper::toOptionResponse)
                .toList();


        List<IngredientResponse> ingredientResponseList = productIngredientRepo.findAllByProductId(product.getId())
                .stream()
                .map(ProductIngredient::getIngredient)
                .toList().stream()
                .map(IngredientMapper.INSTANCE::toIngredientResponse)
                .toList();

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .optionList(optionList)
                .ingredientList(ingredientResponseList)
                .build();
    }
}
