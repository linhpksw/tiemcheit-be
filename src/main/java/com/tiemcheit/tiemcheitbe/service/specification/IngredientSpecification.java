package com.tiemcheit.tiemcheitbe.service.specification;

import com.tiemcheit.tiemcheitbe.model.Ingredient;
import org.springframework.data.jpa.domain.Specification;

import java.util.Map;

public class IngredientSpecification {
    public static Specification<Ingredient> hasName(String name) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"), "%" + name + "%");
    }

    public static Specification<Ingredient> hasStatus(String status) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status);
    }

    public static Specification<Ingredient> getSpecification(Map<String, String> params) {
        Specification<Ingredient> specification = Specification.where(null);

        if (params.containsKey("name")) {
            specification = specification.and(hasName(params.get("name")));
        }

        if (params.containsKey("status")) {
            specification = specification.and(hasStatus(params.get("status")));
        }

        return specification;
    }
}
