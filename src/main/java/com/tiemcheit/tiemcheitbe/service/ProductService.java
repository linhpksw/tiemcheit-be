package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.ProductRequest;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.IngredientMapper;
import com.tiemcheit.tiemcheitbe.mapper.OptionMapper;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.Product;
import com.tiemcheit.tiemcheitbe.model.ProductImage;
import com.tiemcheit.tiemcheitbe.model.ProductIngredient;
import com.tiemcheit.tiemcheitbe.model.ProductOption;
import com.tiemcheit.tiemcheitbe.repository.*;
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
    private final ProductImageRepo productImageRepo;
    private final OptionRepo optionRepo;
    private final IngredientRepo ingredientRepo;

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

        List<String> imageList = productImageRepo.findAllByProductId(product.getId())
                .stream()
                .map(ProductImage::getImage)
                .toList();

        return ProductDetailResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .image1(imageList.get(0))
                .image2(imageList.get(1))
                .image3(imageList.get(2))
                .price(product.getPrice())
                .optionList(optionList)
                .ingredientList(ingredientResponseList)
                .build();
    }

    //create a new product
    public ProductResponse create(ProductRequest product) {

        //save product to product table

        Product savedProduct = productRepo.save(ProductMapper.INSTANCE.toProduct(product));

        //get image list from product request
        List<String> imageList = List.of(product.getImage1(), product.getImage2(), product.getImage3());

        //add image list to product image table with product id
        List<ProductImage> productImages = imageList.stream()
                .map(image -> ProductImage.builder().image(image).product(savedProduct).build())
                .toList();

        if (productImages.size() > 3) {
            throw new AppException("Image list must not exceed 3 images", HttpStatus.BAD_REQUEST);
        }
        productImageRepo.saveAll(productImages);


        //add product options to product option table with product id
        List<ProductOption> productOptions = product.getOptionId().stream()
                .map(optionId -> ProductOption.builder().option(optionRepo.getReferenceById(optionId)).product(savedProduct).build())
                .toList();
        productOptionRepo.saveAll(productOptions);

        //add product ingredients to product ingredient table with product id
        List<ProductIngredient> productIngredients = product.getIngredientId().stream()
                .map(ingredientId -> ProductIngredient.builder().ingredient(ingredientRepo.getReferenceById(ingredientId)).product(savedProduct).build())
                .toList();
        productIngredientRepo.saveAll(productIngredients);

        return ProductMapper.INSTANCE.toProductResponse(savedProduct);
    }

}
