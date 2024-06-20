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

    public static Specification<Product> hasPriceHigher(Double minPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
    }

    public static Specification<Product> hasPriceLower(Double maxPrice) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
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

        if (params.containsKey("minPrice")) {
            Double minPrice = Double.valueOf(params.get("minPrice"));
            specification = specification.and(hasPriceHigher(minPrice));
        }

        if (params.containsKey("maxPrice")) {
            Double maxPrice = Double.valueOf(params.get("maxPrice"));
            specification = specification.and(hasPriceLower(maxPrice));
        }

        if (params.containsKey("categories")) {
            if (params.get("categories").equals("")) {
                specification = specification.and(hasCategories(null));
            } else {
                Set<Long> categoryIds = Arrays.stream(params.get("categories").split(","))
                        .map(Long::valueOf)
                        .collect(Collectors.toSet());
                specification = specification.and(hasCategories(categoryIds));
            }
        }

        if (params.containsKey("status")) {
            specification = specification.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), params.get("status")));
        }
        return specification;
    }
}
