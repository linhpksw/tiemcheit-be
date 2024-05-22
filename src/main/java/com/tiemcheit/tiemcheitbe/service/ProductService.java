package com.tiemcheit.tiemcheitbe.service;

import com.tiemcheit.tiemcheitbe.dto.ProductDTO;
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
    public List<ProductDTO> getAllProductsByCategoryId(Long categoryId) {
        List<Product> products = productRepository.findAllByCategoryId(categoryId);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // convert product to productDTO
    public ProductDTO convertToDto(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setImage(product.getImage());

        return productDTO;
    }
}
