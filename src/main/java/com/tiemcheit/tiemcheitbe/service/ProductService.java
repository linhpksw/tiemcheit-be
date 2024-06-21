package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.request.ProductRequest;
import com.tiemcheit.tiemcheitbe.dto.response.IngredientResponse;
import com.tiemcheit.tiemcheitbe.dto.response.OptionResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductDetailResponse;
import com.tiemcheit.tiemcheitbe.dto.response.ProductResponse;
import com.tiemcheit.tiemcheitbe.exception.AppException;
import com.tiemcheit.tiemcheitbe.mapper.IngredientMapper;
import com.tiemcheit.tiemcheitbe.mapper.OptionMapper;
import com.tiemcheit.tiemcheitbe.mapper.ProductIngredientMapper;
import com.tiemcheit.tiemcheitbe.mapper.ProductMapper;
import com.tiemcheit.tiemcheitbe.model.*;
import com.tiemcheit.tiemcheitbe.repository.*;
import com.tiemcheit.tiemcheitbe.service.specification.ProductSpecification;
import com.tiemcheit.tiemcheitbe.util.SecurityUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
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
    private final UserRepo userRepo;


    private final OptionMapper optionMapper;
    private final ProductIngredientMapper productIngredientMapper;
    private final OrderRepo orderRepo;
    private final OrderDetailRepo orderDetailRepo;

    //=============================================FOR CLIENTS=======================================================
    //get all products by active and disabled status
    public List<ProductResponse> getAllProductsByActiveAndDisabledStatus() {
        return productRepo.findAllByActiveAndDisabledStatus()
                .stream()
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                            .findFirst()
                            .map(ProductImage::getImage)
                            .orElse(null));
                    return productResponse;
                })
                .toList();
    }

    //get All ProductResponse by category id
    public List<ProductResponse> getAllProductsByCategoryId(Long categoryId) {

        List<Product> products = productRepo.findAllByCategoryId(categoryId);
        return products.stream()
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                            .findFirst()
                            .map(ProductImage::getImage)
                            .orElse(null));
                    return productResponse;
                })
                .toList();
    }

    //get all products by conditions and sort
    public List<ProductResponse> getProductByConditionsAndSort(Map<String, String> conditions, String sortField, String sortDirection) {
        Specification<Product> specification = ProductSpecification.getSpecification(conditions);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);

        return productRepo.findAll(specification, sort)
                .stream()
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                            .findFirst()
                            .map(ProductImage::getImage)
                            .orElse(null));
                    return productResponse;
                })
                .toList();

    }

    //get bestseller product
    public List<ProductResponse> getTopBestsellers(int top) {
        Pageable topProduct = PageRequest.of(0, top);
        return productRepo.findTopBestsellers(topProduct)
                .stream()
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).getFirst().getImage());
                    return productResponse;
                })
                .toList();
    }
    //get product of an ingredients
    public List<ProductResponse> getProductsOfIngredient(Long ingredientId) {
        List<ProductIngredient> productIngredients = productIngredientRepo.findAllByIngredientId(ingredientId);
        var productFound = productIngredients
                .stream().
                map(ProductIngredient::getProduct).
                distinct().
                toList();
        return productFound
                .stream()
                .map(ProductMapper.INSTANCE::toProductResponse)
                .toList();
    }

    //get ProductDetailResponse by product id
    public ProductDetailResponse getProductDetailById(Long productId) {
        /*
         * check if product is existed, then check status of product is inactive or not
         * if product is inactive, throw exception and return null
         * => to prevent client get inactive product
         * */
        Product product = productRepo.findById(productId).orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        if (product.getStatus().equals("inactive")) {
            User user = userRepo.findByUsername(SecurityUtils.getCurrentUsername())
                    .orElseThrow(() -> new AppException("User not found", HttpStatus.NOT_FOUND));
            if (!userHasRole(user, "ADMIN")) {
                throw new AppException("Product is inactive", HttpStatus.BAD_REQUEST);
            }
        }
        //map product to ProductDetailResponse
        ProductDetailResponse productDetailResponse = ProductMapper.INSTANCE.toProductDetailResponse(product);

        //get option list, ingredient list, image list of product
        List<OptionResponse> optionList = productOptionRepo.findAllByProductId(product.getId())
                .stream()
                .map(ProductOption::getOption)
                .map(optionMapper::toOptionResponse)
                .toList();

        //get ingredient list of product
        List<IngredientResponse> ingredientResponseList = productIngredientRepo.findAllByProductId(product.getId())
                .stream()
                .map(ProductIngredient::getIngredient)
                .toList().stream()
                .map(IngredientMapper.INSTANCE::toIngredientResponse)
                .toList();

        //get image list of product
        List<String> imageList = productImageRepo.findAllByProductId(product.getId())
                .stream()
                .map(ProductImage::getImage)
                .toList();

        //set option list, ingredient list, image list to productDetailResponse
        productDetailResponse.setOptionList(optionList);
        productDetailResponse.setIngredientList(ingredientResponseList);
        productDetailResponse.setImageList(imageList);

        return productDetailResponse;
    }

    public Page<ProductResponse> getProductsWithPagination(int page, int size) {
        return productRepo.findAll(PageRequest.of(page, size))
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                            .findFirst()
                            .map(ProductImage::getImage)
                            .orElse(null));
                    return productResponse;
                });
    }

    public Page<ProductResponse> getProductsWithPaginationAndSort(int page, int size, Map<String, String> conditions, String sortField, String sortDirection) {
        Specification<Product> specification = ProductSpecification.getSpecification(conditions);
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        Page<Product> productPage = productRepo.findAll(specification, PageRequest.of(page, size, sort));

        return productPage.map(product -> {
            ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
            productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                    .findFirst()
                    .map(ProductImage::getImage)
                    .orElse(null));

            return productResponse;
        });
    }

    public List<ProductResponse> getHistoryOrderProduct() {
        User user = userRepo.findByUsername(SecurityUtils.getCurrentUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderRepo.findAllByUserOrderByIdDesc(user);

        List<Product> distinctProducts = orders.stream()
                .flatMap(order -> orderDetailRepo.findAllByOrderId(order.getId()).stream())
                .map(orderDetail -> productRepo.findById(orderDetail.getProduct().getId())
                        .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND)))
                .distinct()
                .toList();

        return distinctProducts.stream()
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                            .findFirst()
                            .map(ProductImage::getImage)
                            .orElse(null));

                    return productResponse;
                })
                .toList();
    }

    //=============================================FOR ADMINS=======================================================
    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponse> getAllProductsByStatus(String status) {
        return productRepo.findAllByStatus(status)
                .stream()
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                            .findFirst()
                            .map(ProductImage::getImage)
                            .orElse(null));
                    return productResponse;
                })
                .toList();
    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<ProductResponse> getAllProducts() {
        return productRepo.findAll()
                .stream()
                .map(product -> {
                    ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(product);
                    productResponse.setImage(productImageRepo.findAllByProductId(product.getId()).stream()
                            .findFirst()
                            .map(ProductImage::getImage)
                            .orElse(null));
                    return productResponse;
                })
                .toList();
    }

    //create a new product
    @PreAuthorize("hasRole('ADMIN')")
    public ProductResponse create(ProductRequest productRequest) {
        //save product to product table
        Product product = ProductMapper.INSTANCE.toProduct(productRequest);
        if (product == null) {
            throw new AppException("Product is null", HttpStatus.BAD_REQUEST);
        }

        List<String> imageList = productRequest.getImageList();
        product.setCategory(productRequest.getCategory());
        //save product to product table
        Product savedProduct = productRepo.save(product);
        updateProductImages(productRequest, savedProduct);
        updateProductOptions(productRequest, savedProduct);
        updateProductIngredients(productRequest, savedProduct);

        ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(savedProduct);
        productResponse.setImage(imageList.getFirst());
        return productResponse;
    }

    //update a product
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public ProductResponse update(ProductRequest productRequest, Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        if (!productRequest.getName().isEmpty()) {
            product.setName(productRequest.getName());
        }
        if (!productRequest.getDescription().isEmpty()) {
            product.setDescription(productRequest.getDescription());
        }
        if (!productRequest.getStatus().isEmpty()) {
            product.setStatus(productRequest.getStatus());
        }
        if (productRequest.getPrice() != null) {
            product.setPrice(productRequest.getPrice());
        }
        if (productRequest.getQuantity() != null) {
            product.setQuantity(productRequest.getQuantity());
        }

        Product updatedProduct = productRepo.save(product);
        if (productRequest.getImageList() != null) {
            updateProductImages(productRequest, updatedProduct);
        }

        if (productRequest.getOptionId() != null) {
            updateProductOptions(productRequest, updatedProduct);
        }

        if (productRequest.getProductIngredients() != null) {
            updateProductIngredients(productRequest, updatedProduct);
        }

        ProductResponse productResponse = ProductMapper.INSTANCE.toProductResponse(updatedProduct);
        productResponse.setImage(productImageRepo.findAllByProductId(updatedProduct.getId()).getFirst().getImage());
        return productResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Boolean delete(Long id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new AppException("Product not found", HttpStatus.NOT_FOUND));
        productRepo.delete(product);
        return true;
    }

    private void updateProductImages(ProductRequest productRequest, Product product) {
        productImageRepo.deleteAllByProductId(product.getId());
        List<String> imageList = productRequest.getImageList();
        List<ProductImage> productImages = imageList.stream()
                .map(image -> ProductImage.builder().image(image).product(product).build())
                .toList();
        productImageRepo.saveAll(productImages);
    }

    private void updateProductOptions(ProductRequest productRequest, Product product) {
        productOptionRepo.deleteAllByProductId(product.getId());
        List<ProductOption> productOptions = productRequest.getOptionId().stream()
                .map(optionId -> ProductOption.builder().option(optionRepo.getReferenceById(optionId)).product(product).build())
                .toList();
        if (productOptions == null) {
            throw new AppException("Product options is null", HttpStatus.BAD_REQUEST);
        }
        productOptionRepo.saveAll(productOptions);
    }

    private void updateProductIngredients(ProductRequest productRequest, Product product) {

        // Cập nhật vào bảng product_ingredients
        List<ProductIngredient> productIngredients = productRequest.getProductIngredients().stream()
                .map(productIngredientRequest -> {
                    ProductIngredient productIngredient = productIngredientMapper.toProductIngredient(productIngredientRequest);

                    // Lấy đối tượng Ingredient từ ID
                    Ingredient ingredient = ingredientRepo.findById(productIngredientRequest.getIngredient().getId())
                            .orElseThrow(() -> new AppException("Không tìm thấy nguyên liệu", HttpStatus.NOT_FOUND));

                    // Thiết lập các thuộc tính cho productIngredient
                    productIngredient.setIngredient(ingredient);
                    productIngredient.setProduct(product);

                    return productIngredient;
                })
                .toList();

        if (productIngredients.isEmpty()) {
            throw new AppException("Danh sách thành phần sản phẩm trống", HttpStatus.BAD_REQUEST);
        }

        // Lưu danh sách các thành phần sản phẩm mới
        productIngredientRepo.saveAll(productIngredients);
    }

    private boolean userHasRole(User user, String role) {
        return user.getRoles().stream().anyMatch(r -> r.getName().equals(role));
    }
}
