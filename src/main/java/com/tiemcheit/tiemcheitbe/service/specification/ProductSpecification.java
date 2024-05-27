package com.tiemcheit.tiemcheitbe.service.specification;

import com.tiemcheit.tiemcheitbe.model.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductSpecification {
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Product> hasPriceBetween(Double minPrice, Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
    }
     
    public static Specification<Product> hasCategories(Set<Long> categoryIds) {
        return (root, query, criteriaBuilder) -> {
            if (categoryIds == null || categoryIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("category").get("id").in(categoryIds);
        };
    }

    public static Specification<Product> getSpecification(Map<String, String> params) {
        Specification<Product> specification = Specification.where(null);

        if (params.containsKey("name")) {
            specification = specification.and(hasName(params.get("name")));
        }

        if (params.containsKey("minPrice") && params.containsKey("maxPrice")) {
            Double minPrice = Double.valueOf(params.get("minPrice"));
            Double maxPrice = Double.valueOf(params.get("maxPrice"));
            specification = specification.and(hasPriceBetween(minPrice, maxPrice));
        }


        if (params.containsKey("categories")) {
            Set<Long> categoryIds = Arrays.stream(params.get("categories").split(","))
                    .map(Long::valueOf)
                    .collect(Collectors.toSet());
            specification = specification.and(hasCategories(categoryIds));
        }

        return specification;
    }
}

