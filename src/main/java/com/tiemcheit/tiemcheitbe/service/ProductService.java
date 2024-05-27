package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.response.ApiResponse;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.mapper.IngredientMapper;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.Ingredient;
import com.tiemcheit.tiemcheitbe.model.Option;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.repository.ProductIngredientRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductOptionRepo;
import com.tiemcheit.tiemcheitbe.repository.ProductRepo;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepository;
    private final ProductOptionRepo productOptionRepo;
    private final ProductIngredientRepo productIngredientRepo;

    //get All ProductResponse by category id
    public List<ProductResponse> getAllProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return products.stream()
                .map(ProductMapper.INSTANCE::toProductResponse)
                .collect(Collectors.toList());
    }

    //get ProductDetailResponse by product id
    public ProductDetailResponse getProductDetailById(Long productId) {
         Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        List<Option> optionList = productOptionRepo.findAllByProductId(productId)
                .stream()
                .map(productOption -> Option.builder()
                        .id(productOption.getOption().getId())
                        .name(productOption.getOption().getName())
                        .build())
                .toList();

        List<IngredientResponse> ingredientResponseList = productIngredientRepo.findAllByProductId(productId)
                .stream()
                .map(productIngredient -> Ingredient.builder()
                        .id(productIngredient.getIngredient().getId())
                        .name(productIngredient.getIngredient().getName())
                        .build())
                .toList().stream()
                .map(IngredientMapper.INSTANCE::toIngredientResponse)
                .toList();

        ProductDetailResponse productDetailResponse = ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .image(product.getImage())
                .price(product.getPrice())
                .optionList(optionList)
                .ingredientList(ingredientResponseList)
                .build();

        return productDetailResponse;



    }




}
